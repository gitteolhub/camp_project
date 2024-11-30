// 고정된 키워드로 검색 수행 함수
function searchByCate3Name(keyword, page, limit) {
	
    // 검색 결과 영역 초기화
    const results = document.getElementById('results');
    results.innerHTML = ""; // 기존 결과 초기화

    // 카테고리 검색 요청
    fetch(`/campProject/searchCate3Name?cate3=${encodeURIComponent(keyword)}&page=${page}&limit=${limit}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('서버 응답이 정상적이지 않습니다.');
        }
        return response.json(); // JSON 응답 처리
    })
    .then(data => {
        // 데이터가 없으면 메시지 출력
        if (!data || data.length === 0) {
            results.innerHTML = `<p>${keyword}에 대한 결과가 없습니다.</p>`;
            return;
        }
		
		console.log("data.campList:"+data.campList.length);
		console.log("data.pageVO:"+data.pageVO);
		
		console.log("data.startPage:"+data.pageVO.startPage);
		console.log("data.prePage:"+data.pageVO.prePage);
		console.log("data.currPage:"+data.pageVO.currPage);
		console.log("data.nextPage:"+data.pageVO.nextPage);
		console.log("data.maxPage:"+data.pageVO.maxPage);
		
		console.log("data.campList:"+data.campList[0].roadAddress);
		
		// 페이징 버튼에 페이지 할당
		let pagingContent=
				   `<li><a class="pagingButton" href="javascript:searchByCate3Name('${keyword}', ${data.pageVO.startPage}, 10)"><i title="${data.pageVO.startPage}" class="bi bi-chevron-double-left"></i></a></li>
				    <li><a class="pagingButton" href="javascript:searchByCate3Name('${keyword}', ${data.pageVO.prePage}, 10)"><i title="${data.pageVO.prePage}" class="bi bi-chevron-left"></i></a></li>
					
					<li><a title="${data.pageVO.currPage}" class="pagingButton" href="javascript:searchByCate3Name('${keyword}', ${data.pageVO.currPage}, 10)">${data.pageVO.currPage}</a></li>
					
					<li><a class="pagingButton" href="javascript:searchByCate3Name('${keyword}', ${data.pageVO.nextPage}, 10)"><i title="${data.pageVO.nextPage}" class="bi bi-chevron-right"></i></a></li>
					<li><a class="pagingButton" href="javascript:searchByCate3Name('${keyword}', ${data.pageVO.maxPage}, 10)"><i id="maxPage" title="${data.pageVO.maxPage}" class="bi bi-chevron-double-right"></i></a></li>
				   `;
				   
		document.querySelector("#pagingSectionTop ul#pagingsTop").innerHTML=pagingContent;
		document.querySelector("#pagingSectionBottom ul#pagingsBottom").innerHTML=pagingContent;
		
		
        // 결과를 표시할 HTML 초기화
        let resultsHTML = `<h4>카테고리: ${keyword}</h4>`;

        // 데이터 행 추가
        data.campList.forEach(camp => {
            const address = camp.roadAddress ? camp.roadAddress : camp.jibunAddress; // 주소 처리
            
            let campImg = camp.mainImg=='' || camp.mainImg==null ? 'noimg.jpg' : `${camp.mainImg}`;

            resultsHTML += `
                <div style="margin-bottom: 20px; border: 1px solid #ddd; padding: 10px; border-radius: 8px;">
					<p><img src="/campProject/campImg/${campImg}"></p>                	
                    <h3> ${camp.campName}</h3>
                    <p><strong>종류:</strong> ${camp.cate3}</p>
                    <p><strong>주소:</strong> ${address}</p>
                    <p><strong>시설 특징:</strong> ${camp.facilCharacteristics}</p>
                    <p><strong>시설 상세:</strong> ${camp.facilDetail}</p>
                </div>
            `;
        });
        

        // 결과를 HTML로 표시
        results.innerHTML = resultsHTML;
    })
    .catch(error => {
        console.error('추가 검색 중 오류가 발생했습니다:', error);
        results.innerHTML = `<p>추가 검색 중 오류가 발생했습니다.</p>`;
    });
} // 카테고리 검색 요청

// 캠핑 이름 검색 요청
window.onload = () => {
	
	let campName = document.getElementById('campName');
	
	campName.addEventListener('keyup', function(event) {
		
		if(window.event.keyCode==13){ // 엔터키 감지
			 event.preventDefault(); // 폼 제출 시 페이지 새로고침을 방지
	
		    // 입력된 캠핑장 이름을 가져오기
		    const campName = document.getElementById('campName').value.trim();
		
		    // 입력값이 비어있는 경우 메시지를 표시
		    if (!campName) {
		        alert("캠핑장 이름을 입력하세요!");
		        return;
		    }
		
		    // 검색 결과 영역 초기화
		    document.getElementById('results').innerHTML = "";
		
		    // AJAX 요청 보내기
		    fetch(`/campProject/searchCamp?campName=${encodeURIComponent(campName)}`, {
		        method: 'GET',
		        headers: {
		            'Content-Type': 'application/json',
		        },
		    })
		    .then(response => {
		        if (!response.ok) {
		            throw new Error('서버 응답이 정상적이지 않습니다.');
		        }
		        return response.json();  // 응답을 JSON으로 받음
		    })
		    .then(data => {
		        // 데이터가 없으면 메시지 출력
		        if (!data || data.length === 0) {
		            document.getElementById('results').innerHTML = "<p>결과가 없습니다.</p>";
		            return;
		        }
		
		        // 결과를 표시할 HTML을 초기화
		        let resultsHTML = "";
		
		        // 데이터 행 추가
		        data.campList.forEach(camp => {
		            // roadAddress가 null일 경우 jibunAddress를 표시
		            const address = camp.roadAddress ? camp.roadAddress : camp.jibunAddress;
		
					let campImg = camp.mainImg=='' || camp.mainImg==null ? 'noimg.jpg' : `${camp.mainImg}`;
		
		            // HTML 생성
		            resultsHTML += `
		                <div style="margin-bottom: 20px; border: 1px solid #ddd; padding: 10px; border-radius: 8px; min-width:500px;">
		                    <p><img src="/campProject/campImg/${campImg}" style="width:100%"></p>  
		                    <h3> ${camp.campName}</h3>
		                    <p><strong>종류:</strong> ${camp.cate3}</p>
		                    <p><strong>주소:</strong> ${address}</p>
		                    <p><strong>시설 특징:</strong> ${camp.facilCharacteristics}</p>
		                    <p><strong>시설 상세:</strong> ${camp.facilDetail}</p>
		                </div>
		            `;
		        });
		
		        // 결과를 HTML로 표시
		        document.getElementById('results').innerHTML = resultsHTML;
		    })
		    .catch(error => {
		        console.error('검색 중 오류가 발생했습니다:', error);
		        document.getElementById('results').innerHTML = `<p>검색 중 오류가 발생했습니다.</p>`;
		    });
		}
	});
	
	document.getElementById('sendBtn').addEventListener('click', function(event) {
	    event.preventDefault(); // 폼 제출 시 페이지 새로고침을 방지
	
	    // 입력된 캠핑장 이름을 가져오기
	    const campName = document.getElementById('campName').value.trim();
	
	    // 입력값이 비어있는 경우 메시지를 표시
	    if (!campName) {
	        alert("캠핑장 이름을 입력하세요!");
	        return;
	    }
	
	    // 검색 결과 영역 초기화
	    document.getElementById('results').innerHTML = "";
	
	    // AJAX 요청 보내기
	    fetch(`/campProject/searchCamp?campName=${encodeURIComponent(campName)}`, {
	        method: 'GET',
	        headers: {
	            'Content-Type': 'application/json',
	        },
	    })
	    .then(response => {
	        if (!response.ok) {
	            throw new Error('서버 응답이 정상적이지 않습니다.');
	        }
	        return response.json();  // 응답을 JSON으로 받음
	    })
	    .then(data => {
	        // 데이터가 없으면 메시지 출력
	        if (!data || data.length === 0) {
	            document.getElementById('results').innerHTML = "<p>결과가 없습니다.</p>";
	            return;
	        }
	
	        // 결과를 표시할 HTML을 초기화
	        let resultsHTML = "";
	
	        // 데이터 행 추가
	        data.campList.forEach(camp => {
	            // roadAddress가 null일 경우 jibunAddress를 표시
	            const address = camp.roadAddress ? camp.roadAddress : camp.jibunAddress;
	
				let campImg = camp.mainImg=='' || camp.mainImg==null ? 'noimg.jpg' : `${camp.mainImg}`;
	
	            // HTML 생성
	            resultsHTML += `
	                <div style="margin-bottom: 20px; border: 1px solid #ddd; padding: 10px; border-radius: 8px; min-width:500px;">
	                    <p><img src="/campProject/campImg/${campImg}" style="width:100%"></p>  
	                    <h3> ${camp.campName}</h3>
	                    <p><strong>종류:</strong> ${camp.cate3}</p>
	                    <p><strong>주소:</strong> ${address}</p>
	                    <p><strong>시설 특징:</strong> ${camp.facilCharacteristics}</p>
	                    <p><strong>시설 상세:</strong> ${camp.facilDetail}</p>
	                </div>
	            `;
	        });
	
	        // 결과를 HTML로 표시
	        document.getElementById('results').innerHTML = resultsHTML;
	    })
	    .catch(error => {
	        console.error('검색 중 오류가 발생했습니다:', error);
	        document.getElementById('results').innerHTML = `<p>검색 중 오류가 발생했습니다.</p>`;
	    });
	    
	}); // 캠핑 이름 검색 요청

	
	// 키워드 버튼 클릭 이벤트 추가
	let searchCamps = document.querySelectorAll('.searchCamps');
		
	for (let searchCamp of searchCamps){
		
		searchCamp.addEventListener('click', (e) => { 
			
			let cate3 = e.target.alt;
			console.log(`${cate3} 검색`);
			
			/*let page = document.getElementById('page').value;*/
			let page = 1;
			let limit = 10;
		    searchByCate3Name(`${cate3}`, page, limit);
		});
	
	}
}