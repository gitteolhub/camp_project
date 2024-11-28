document.getElementById('searchForm').addEventListener('submit', function(event) {
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
        data.forEach(camp => {
            // roadAddress가 null일 경우 jibunAddress를 표시
            const address = camp.roadAddress ? camp.roadAddress : camp.jibunAddress;

            // HTML 생성
            resultsHTML += `
                <div style="margin-bottom: 20px; border: 1px solid #ddd; padding: 10px; border-radius: 8px;">
                    <h3> ${camp.campName}</h3>
                    <p><strong>종류:</strong> ${camp.cate3}</p>
                    <p><strong>도로명 주소:</strong> ${address}</p>
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
});
