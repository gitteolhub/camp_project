// 캠핑 검색 함수
function searchCamping() {
    const keyword = document.getElementById('keyword').value;
    if (!keyword) {
        alert("검색어를 입력해주세요!");
        return;
    }

    // API 호출
    fetch(`/campProject/searchCamping?keyword=${encodeURIComponent(keyword)}`)
    .then(response => {
		console.log("response:"+response);
		console.log("response.data:"+response.response);
        /*if (!response.ok) {
            throw new Error(`HTTP 오류! 상태 코드: ${response.status}`);
        }
        return response.json();*/
    })
    .then(data => {
        displayResults(data);
    })
    .catch(error => {
        console.error('API 호출 오류:', error);
        alert("검색 중 오류가 발생했습니다.");
    });
}

// 검색 결과 표시 함수
function displayResults(data) {
    const resultsContainer = document.getElementById('results');
    resultsContainer.innerHTML = ''; // 기존 결과 초기화

    if (!data || data.length === 0) {
        resultsContainer.innerHTML = '<p>검색 결과가 없습니다.</p>';
        return;
    }

    // 검색된 캠핑지 결과를 표시
    data.forEach(item => {
        const resultItem = document.createElement('div');
        resultItem.classList.add('result-item');
        resultItem.innerHTML = `
            <h3>${item.name}</h3>
            <p>${item.address}</p>
            <p>${item.description}</p>
        `;
        resultsContainer.appendChild(resultItem);
    });
}