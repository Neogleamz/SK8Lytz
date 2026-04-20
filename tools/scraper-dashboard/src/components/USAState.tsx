// @ts-nocheck
import React, { useRef, useEffect, useState } from "react";

const USAState = (props) => {
  const pathRef = useRef(null);
  const [center, setCenter] = useState(null);

  useEffect(() => {
    if (pathRef.current) {
      const bbox = pathRef.current.getBBox();
      setCenter({
        x: bbox.x + bbox.width / 2,
        y: bbox.y + bbox.height / 2
      });
    }
  }, [props.dimensions]);

  return (
    <g>
      <path ref={pathRef} d={props.dimensions} fill={props.fill} data-name={props.state} className={`${props.state} state`} onClick={props.onClickState}>
        <title>{props.stateName} {props.customString ? `(${props.customString} records)` : ''}</title>
      </path>
      {center && props.customString && (
        <text 
          x={center.x} 
          y={center.y} 
          textAnchor="middle" 
          dominantBaseline="middle" 
          fill="#FFF" 
          fontSize="11" 
          fontWeight="bold" 
          style={{ pointerEvents: 'none', textShadow: '0px 0px 4px #000, 0px 0px 8px rgba(0,0,0,0.8)' }}
        >
          {props.customString}
        </text>
      )}
    </g>
  );
}
export default USAState;
