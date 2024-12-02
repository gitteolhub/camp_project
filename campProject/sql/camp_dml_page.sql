-- 페이징 카테고리 검색
SELECT  *
  FROM ( SELECT m.*  
                , FLOOR((ROWNUM - 1) / 10 + 1) PAGE  
           FROM ( SELECT *
                    FROM camp_info_tbl
                   WHERE cate3 LIKE '%일반야영장%'
                   ORDER BY ID DESC ) m
       )  
 WHERE PAGE = 1;
 
 SELECT count(*) FROM camp_info_tbl
		WHERE cate3 LIKE '%일반야영장%';