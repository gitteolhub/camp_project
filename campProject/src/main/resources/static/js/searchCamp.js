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
.then(response => response.text())  // JSON 대신 응답을 텍스트로 처리
.then(text => {
    // 받은 텍스트를 그대로 HTML로 처리하거나 필요한 방식으로 파싱
    console.log("서버 응답:", text);
    
    // 예를 들어, 텍스트가 HTML이라면 화면에 표시할 수 있습니다.
    document.getElementById('results').innerHTML = text;
})
.catch(error => {
    console.error('검색 중 오류가 발생했습니다:', error);
    document.getElementById('results').innerHTML = `<p>검색 중 오류가 발생했습니다.</p>`;
});
});