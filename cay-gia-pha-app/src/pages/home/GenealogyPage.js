import React, { useState, useCallback, useEffect } from 'react';
import ReactFlow, {
      MiniMap,
      Controls,
      Background,
      useNodesState,
      useEdgesState
} from 'reactflow';
import 'reactflow/dist/style.css';
import './GenealogyPage.css'; // File CSS của bạn (đã có zIndex: 1 cho overlay)
import MemberFormModal from '../../components/genealogy/MemberFormModal';
import RelationshipFormModal from '../../components/genealogy/RelationshipFormModal';
import homepageBg from '../../assets/homepage.jpg';
import GenealogyNode from '../../components/genealogy/GenealogyNode';
import {
      deleteRelationship,
      addRelationship,
      getMembersList,
      addMember,
      updateMember,
      deleteMember
} from '../../api/apiConfig';

// 1. IMPORT DAGRE
import dagre from 'dagre';

// (Hàm format giữ nguyên)
const formatNodeLabel = (data) => {
      let label = `${data.name} (${data.sex ? "Nam" : "Nữ"})`;
      if (data.dob) label += `\n${data.dob}`;
      if (data.dod) label += `\n${data.dod}`;
      return label;
};
const nodeTypes = {
      custom: GenealogyNode, // Tên 'custom' sẽ dùng trong data
};
// 2. SỬA LẠI: Hàm Style (tắt mũi tên cho quan hệ 2 chiều)
const getEdgeStyle = (label) => {
      const style = { strokeWidth: 2 };
      switch (label) {
            case 'vợ-chồng':
                  style.stroke = 'red';
                  style.markerEnd = 'none'; // Tắt mũi tên
                  break;
            case 'anh-em':
                  style.stroke = 'blue';
                  style.markerEnd = 'none'; // Tắt mũi tên
                  break;
            case 'cha-con':
            case 'mẹ-con':
                  style.stroke = 'hotpink';
                  // Giữ mũi tên mặc định
                  break;
            default:
                  style.stroke = '#b1b1b7';
      }
      return style;
};

// (Map nhãn giữ nguyên)
const relationshipTypeMap = {
      'vợ-chồng': 'SOUSE',
      'cha-con': 'FATHER_SON',
      'mẹ-con': 'MOTHER_SON',
      'anh-em': 'BROTHER_SISTER',
};

const reverseRelationshipTypeMap = {
      'SOUSE': 'vợ-chồng',
      'FATHER_SON': 'cha-con',
      'MOTHER_SON': 'mẹ-con',
      'BROTHER_SISTER': 'anh-em',
};

// 3. Khai báo kích thước Node cho Dagre
const NODE_WIDTH = 200;
const NODE_HEIGHT = 100;

// 4. HÀM LAYOUT BẰNG DAGRE (Quan trọng nhất)
// Hàm này sẽ xếp "cha-con" ở 2 tầng khác nhau
// ... (các đoạn import giữ nguyên)

// 4. HÀM LAYOUT BẰNG DAGRE (Phiên bản ép ngang hàng tuyệt đối)
const getLayoutedElements = (nodes, edges) => {
      const g = new dagre.graphlib.Graph();
      g.setGraph({ rankdir: 'TB', nodesep: 200, ranksep: 100 });
      g.setDefaultEdgeLabel(() => ({}));

      // 1. Thêm Nodes vào Dagre
      nodes.forEach((node) => {
            g.setNode(node.id, { width: NODE_WIDTH, height: NODE_HEIGHT });
      });

      // 2. Thêm Edges vào Dagre
      edges.forEach((edge) => {
            const isHorizontal = edge.label === 'vợ-chồng' || edge.label === 'anh-em';

            if (isHorizontal) {
                  // constraint: false giúp Dagre không đẩy node xuống tầng dưới
                  g.setEdge(edge.source, edge.target, { constraint: false });
            } else {
                  g.setEdge(edge.source, edge.target);
            }
      });

      // 3. Chạy tính toán vị trí
      dagre.layout(g);

      // 4. Lấy vị trí từ Dagre ra
      const layoutedNodes = nodes.map((node) => {
            const nodeWithPosition = g.node(node.id);
            return {
                  ...node,
                  position: {
                        x: nodeWithPosition.x - NODE_WIDTH / 2,
                        y: nodeWithPosition.y - NODE_HEIGHT / 2,
                  },
            };
      });

      // --- BƯỚC MỚI: HẬU XỬ LÝ (ÉP NGANG HÀNG) ---
      // Tạo Map để tìm node nhanh hơn
      const nodeMap = new Map(layoutedNodes.map(n => [n.id, n]));

      edges.forEach((edge) => {
            const isHorizontal = edge.label === 'vợ-chồng' || edge.label === 'anh-em';

            // Nếu là quan hệ ngang hàng
            if (isHorizontal) {
                  const sourceNode = nodeMap.get(edge.source);
                  const targetNode = nodeMap.get(edge.target);

                  if (sourceNode && targetNode) {
                        // BẮT BUỘC: Gán tọa độ Y của node đích = tọa độ Y của node nguồn
                        targetNode.position.y = sourceNode.position.y;
                  }
            }
      });
      // ---------------------------------------------

      return { nodes: layoutedNodes, edges: edges };
};
// 5. HÀM TRANSFORM (đã bỏ logic position thủ công + LỌC TRÙNG LẶP)
const transformDataForReactFlow = (apiData) => {
      // A. Chuyển đổi Nodes -> Dùng type 'custom'
      const transformedNodes = apiData.result.nodes.map((node) => ({
            id: node.id,
            type: 'custom', // <--- DÙNG CUSTOM NODE
            data: {
                  ...node,
                  // Dữ liệu này sẽ được GenealogyNode dùng để hiển thị
                  name: node.name,
                  sex: node.sex === true ? "Nam" : "Nữ",
                  dob: node.dob
            },
            style: {
            // Ví dụ: Nam (true) màu xanh nhạt, Nữ (false) màu hồng nhạt
            backgroundColor: node.sex ? '#e3f2fd' : '#fce4ec', 
            border: node.sex ? '1px solid #2196f3' : '1px solid #e91e63',
            borderRadius: '5px',
            // ... các style khác nếu cần
        }
      }));

      // B. Chuyển đổi Edges -> Chỉ định Handle ID
      const transformedEdges = [];
      const processedPairs = new Set();

      for (const rel of apiData.result.relations) {
            const label = reverseRelationshipTypeMap[rel.type] || rel.type;

            // (Logic lọc trùng lặp giữ nguyên...)
            if (rel.type === 'SOUSE' || rel.type === 'BROTHER_SISTER') {
                  const key = [rel.fromNodeId, rel.toNodeId].sort().join('-');
                  if (processedPairs.has(key)) continue;
                  processedPairs.add(key);
            }

            // --- LOGIC CHỌN LỖ CẮM (HANDLE) ---
            let sourceHandle = 'bottom'; // Mặc định nối từ dưới
            let targetHandle = 'top';    // Mặc định cắm vào trên

            if (rel.type === 'SOUSE' || rel.type === 'BROTHER_SISTER') {
                  // Nếu là Vợ chồng / Anh em -> Nối ngang (Phải sang Trái)
                  sourceHandle = 'right';
                  targetHandle = 'left';
            }
            // Nếu là Cha con -> Giữ nguyên Bottom -> Top

            transformedEdges.push({
                  id: rel.id,
                  source: rel.fromNodeId,
                  target: rel.toNodeId,
                  label: label,
                  type: 'smoothstep',
                  pathOptions: { borderRadius: 20 },
                  style: getEdgeStyle(label),

                  // THÊM 2 DÒNG NÀY:
                  sourceHandle: sourceHandle,
                  targetHandle: targetHandle,
            });
      }

      return { nodes: transformedNodes, edges: transformedEdges };
};

// 6. PHẦN COMPONENT (dùng logic Dagre)
const GenealogyPage = () => {
      const [nodes, setNodes, onNodesChange] = useNodesState([]);
      const [edges, setEdges, onEdgesChange] = useEdgesState([]);
      const [loading, setLoading] = useState(true);

      const [isMemberModalOpen, setIsMemberModalOpen] = useState(false);
      const [currentNodeData, setCurrentNodeData] = useState(null);
      const [isRelationshipModalOpen, setIsRelationshipModalOpen] = useState(false);

      // Tách hàm fetchTree để tái sử dụng
      const fetchTree = useCallback(async () => {
            try {
                  setLoading(true);
                  const treeData = await getMembersList();

                  if (treeData.result && treeData.result.nodes && treeData.result.relations) {
                        // Bước 1: Chuyển đổi + Lọc (Hàm mới)
                        const { nodes: transformedNodes, edges: transformedEdges } = transformDataForReactFlow(treeData);

                        // Bước 2: Tính toán layout
                        const { nodes: layoutedNodes, edges: layoutedEdges } = getLayoutedElements(transformedNodes, transformedEdges);

                        // Bước 3: Set state
                        setNodes(layoutedNodes);
                        setEdges(layoutedEdges);
                  }
            } catch (error) {
                  console.error("Lỗi tải cây gia phả:", error);
                  alert("Không thể tải dữ liệu gia phả. Vui lòng thử lại.");
            } finally {
                  setLoading(false);
            }
      }, [setNodes, setEdges]);


      // useEffect sẽ gọi hàm layout tự động
      useEffect(() => {
            fetchTree();
      }, [fetchTree]);


      // (Các hàm mở/đóng modal giữ nguyên)
      const handleOpenRelationshipModal = () => setIsRelationshipModalOpen(true);
      const handleCloseRelationshipModal = () => setIsRelationshipModalOpen(false);

      // --- Các hàm Submit (Sẽ tự động tải lại cây để xếp lại) ---
      const handleRelationshipSubmit = async (sourceId, targetId, typeLabel) => {
            const apiType = relationshipTypeMap[typeLabel];
            if (!apiType) { alert('Loại quan hệ không hợp lệ!'); return; }
            try {
                  const relationData = {
                        fromNodeId: sourceId,
                        toNodeId: targetId,
                        type: apiType
                  };
                  await addRelationship(relationData);
                  fetchTree(); // Tải lại để sắp xếp
                  handleCloseRelationshipModal();
            } catch (error) {
                  alert('Lỗi: Không thể tạo quan hệ.');
            }
      };


      const handleDeleteEdge = async (edgeId) => {
            try {
                  console.log("relations: ",edgeId);
                  await deleteRelationship(edgeId);
                  fetchTree(); // Tải lại để sắp xếp
            } catch (error) {
                  alert('Lỗi: Không thể xóa quan hệ.');
            }
      };
      const onEdgeClick = useCallback((event, edge) => {
            event.stopPropagation();
            // Tạm thời comment logic xóa, vì API của bạn bị trùng ID
            if (window.confirm(`Bạn có chắc muốn xóa mối quan hệ "${edge.label}"?`)) {
              handleDeleteEdge(edge.id);
            }
            console.log("Clicked edge:", edge);
      }, []);

      const handleOpenAddModal = () => { setCurrentNodeData(null); setIsMemberModalOpen(true); };
      const onNodeClick = useCallback((event, node) => { setCurrentNodeData({ id: node.id, ...node.data }); setIsMemberModalOpen(true); }, []);
      const handleCloseMemberModal = () => { setIsMemberModalOpen(false); setCurrentNodeData(null); };

      const handleMemberFormSubmit = async (formData) => {
            try {
                  if (formData.id) {
                        await updateMember(formData.id, formData);
                  } else {
                        await addMember(formData);
                  }
                  fetchTree(); // Tải lại để sắp xếp
                  handleCloseMemberModal();
            } catch (error) {
                  alert(`Lỗi: Không thể ${formData.id ? 'cập nhật' : 'thêm'} thành viên.`);
            }
      };

      const handleDeleteNode = async (nodeId) => {
            try {
                  await deleteMember(nodeId);
                  fetchTree(); // Tải lại để sắp xếp
                  handleCloseMemberModal();
            } catch (error) {
                  alert("Lỗi: Không thể xóa thành viên.");
            }
      };

      // (Phần Loading giữ nguyên)
      if (loading) {
            return (
                  <div style={{ display: 'grid', placeItems: 'center', height: 'calc(100vh - 60px)', fontSize: '1.2rem' }}>
                        Đang tải dữ liệu gia phả...
                  </div>
            );
      }

      // 7. SỬA LỖI Z-INDEX (Click)
      // Overlay (từ CSS của bạn) đã là zIndex: 1
      return (
            <div className="genealogy-container"
                  style={{ backgroundImage: `url(${homepageBg})` }} >

                  <div className="genealogy-overlay"></div>

                  {/* Đặt zIndex: 3 (cao hơn ReactFlow) */}
                  <div className="toolbar" style={{ padding: '10px', background: '#f0f0f0', display: 'flex', gap: '10px', zIndex: 3 }}>
                        <button onClick={handleOpenAddModal}>
                              Thêm thành viên (Node)
                        </button>
                        <button onClick={handleOpenRelationshipModal}>
                              Tạo quan hệ (Edge)
                        </button>
                  </div>

                  {/* Đặt zIndex: 2 (cao hơn Overlay) */}
                  <ReactFlow
                        nodes={nodes}
                        edges={edges}
                        nodeTypes={nodeTypes}
                        onNodeClick={onNodeClick}
                        onEdgeClick={onEdgeClick}
                        onNodesChange={onNodesChange}
                        onEdgesChange={onEdgesChange}
                        fitView
                        style={{ zIndex: 2 }}
                        nodesDraggable={true}
                        panOnDrag={true}
                        nodesConnectable={false}
                  >
                        <MiniMap />
                        <Controls />
                        <Background />
                  </ReactFlow>

                  {/* Modals sẽ tự động có zIndex cao nhất */}
                  <MemberFormModal
                        isOpen={isMemberModalOpen}
                        onClose={handleCloseMemberModal}
                        onSubmit={handleMemberFormSubmit}
                        onDelete={handleDeleteNode}
                        initialData={currentNodeData}
                  />
                  <RelationshipFormModal
                        isOpen={isRelationshipModalOpen}
                        onClose={handleCloseRelationshipModal}
                        onSubmit={handleRelationshipSubmit}
                        nodes={nodes}
                  />
            </div>
      );
};

export default GenealogyPage;