<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<h1>💊 똑디 (Tokdi) Application</h1>

<p><strong>똑디</strong>는 개인 맞춤형 의약품 추천 및 복용 기록 관리를 제공하는 애플리케이션입니다. 사용자에게 최적의 건강 관리 솔루션을 제공하기 위해 AI 기술을 활용한 다양한 기능을 지원합니다.</p>

<h2>📋 프로젝트 개요</h2>
<p>이 프로젝트는 클라이언트(애플리케이션)와 서버로 나뉘며, 사용자가 데이터를 주고받기 위한 다양한 컴포넌트를 구현합니다. 서버는 일반적인 데이터 처리와 AI 모듈을 통한 데이터 처리를 분리하여 더욱 효율적으로 동작합니다.</p>
<p>각 모듈과 컴포넌트의 기능을 상세히 기술하여, 이 설계서를 바탕으로 충분한 기술 스택을 가진 개발자라면 문제없이 구현할 수 있도록 설계되었습니다.</p>

<h2>🚀 주요 기능</h2>
<ul>
  <li><span class="emoji">🩺</span><strong>개인 맞춤형 의약품 추천:</strong> 사용자의 <strong>몸무게</strong>, <strong>연령</strong>, <strong>성별</strong> 등을 기반으로 최적의 의약품을 추천합니다. AI 기술을 활용하여 맞춤형 <strong>영양소</strong>, <strong>약물</strong>, <strong>처치법</strong>을 제안합니다.</li>
  <li><span class="emoji">🔍</span><strong>알약 검색 및 정보 제공:</strong> 사용자가 직접 촬영한 <strong>알약 이미지</strong> 또는 <strong>알약 특징</strong>을 입력하면 해당 약물의 상세 정보를 제공합니다. <strong>복용법</strong>, <strong>주의 사항</strong> 등 필수 정보를 포함한 데이터 제공.</li>
  <li><span class="emoji">💡</span><strong>복용 기록 관리:</strong> <strong>복용 알림</strong> 기능을 통해 사용자가 약물을 올바르게 복용할 수 있도록 돕습니다.</li>
  <li><span class="emoji">📅</span><strong>진료 일정 및 기록 관리:</strong> <strong>진료 일정 알림</strong> 기능을 통해 병원 방문 일정을 잊지 않도록 합니다.</li>
  <li><span class="emoji">📊</span><strong>효능 통계 데이터 제공:</strong> 사용자의 데이터를 기반으로 한 <strong>약물의 효능 통계</strong>를 제공하여, 약물 선택 시 참고할 수 있는 정보를 제공합니다.</li>
</ul>

<h2>🛠 소프트웨어 구조</h2>

<h3>Top-Level 구조:</h3>
<ol>
  <li><strong>클라이언트 (애플리케이션):</strong> 사용자와 데이터를 주고받는 컴포넌트로 구성됩니다.</li>
  <li><strong>서버:</strong> 
    <ul>
      <li>일반 데이터 처리 및 AI 모듈을 통한 데이터 처리로 구분됩니다.</li>
      <li>다양한 모듈 간의 협업과 외부 API와의 통신을 통해 클라이언트에 데이터를 전달합니다.</li>
    </ul>
  </li>
  <li><strong>AI 모듈:</strong> 사용되는 AI 모듈과 해당 모듈의 기능을 상세히 설명합니다. 각 모듈의 내부 Class 구성도와, 멤버 함수 및 데이터를 기술합니다.</li>
</ol>

</body>
</html>
