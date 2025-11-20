import React, { memo } from 'react';
import { Handle, Position } from 'reactflow';

// CSS cho Node tùy chỉnh
const nodeStyle = {
  padding: '10px',
  borderRadius: '5px',
  border: '1px solid #777',
  background: '#fff',
  minWidth: '150px',
  textAlign: 'center',
  fontSize: '12px',
  position: 'relative',
};

const labelStyle = {
  fontWeight: 'bold',
  marginBottom: '5px',
  display: 'block',
};

const GenealogyNode = ({ data }) => {
  return (
    <div style={nodeStyle} className="genealogy-node">
      {/* --- 4 ĐIỂM KẾT NỐI (HANDLES) --- */}

      {/* 1. Handle TRÊN (Nhận kết nối từ Cha Mẹ) - ID: 'top' */}
      <Handle 
        type="target" 
        position={Position.Top} 
        id="top" 
        style={{ background: '#555' }} 
      />

      {/* 2. Handle DƯỚI (Nối xuống Con Cái) - ID: 'bottom' */}
      <Handle 
        type="source" 
        position={Position.Bottom} 
        id="bottom" 
        style={{ background: '#555' }} 
      />

      {/* 3. Handle TRÁI (Nhận kết nối Vợ/Chồng) - ID: 'left' */}
      <Handle 
        type="target" 
        position={Position.Left} 
        id="left" 
        style={{ background: 'red' }} // Màu đỏ để dễ nhận biết quan hệ vợ chồng
      />

      {/* 4. Handle PHẢI (Nối ra Vợ/Chồng) - ID: 'right' */}
      <Handle 
        type="source" 
        position={Position.Right} 
        id="right" 
        style={{ background: 'red' }} 
      />

      {/* --- NỘI DUNG NODE --- */}
      <span style={labelStyle}>{data.name} ({data.sex})</span>
      <div>{data.dob || '???'}</div>
    </div>
  );
};

export default memo(GenealogyNode);