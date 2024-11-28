document.getElementById('searchBtn').addEventListener('click', function() {
    // 사용자 입력값 가져오기
    const campName = document.getElementById('campName').value.trim();

    // 결과 영역 초기화
    const resultContainer = document.getElementById('resultContainer');
    resultContainer.innerHTML = '';

    // 캠핑 이름이 비어 있지 않으면 검색 API 호출
    if (campName) {
        searchCampingByName(campName);
    } else {
        resultContainer.innerHTML = '<p>검색어를 입력해주세요.</p>';
    }
});

// 캠핑 이름으로 검색
function searchCampingByName(campName) {
    fetch(`/campByName?campName=${campName}`)
        .then(response => response.json())
        .then(data => displayResults(data))
        .catch(error => console.error('Error:', error));
}

// 검색 결과 표시
function displayResults(data) {
    const resultContainer = document.getElementById('resultContainer');

    if (data && data.length > 0) {
        const ul = document.createElement('ul');
        data.forEach(camp => {
            const li = document.createElement('li');
            li.innerHTML = `
                <strong>캠핑 이름:</strong> ${camp.campName}<br>`;
            ul.appendChild(li);
        });
        resultContainer.appendChild(ul);
    } else {
        resultContainer.innerHTML = '<p>검색된 결과가 없습니다.</p>';
    }
}
