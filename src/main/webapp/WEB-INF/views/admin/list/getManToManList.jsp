<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="w3-container">
	<input type="hidden" value="${check}" id="qnaNACheck">
	<div class="topnav">
		<a class="check active" title="manToman" style="cursor: pointer;">1:1 문의</a>
		<select class="form-control" style="width: 180px; height: 52px; float: left;" id="qnaKind">
				<option value="">문의 종류 별 검색</option>
				<option value="계정문의">계정문의</option>
				<option value="차단">차단(제제)</option>
				<option value="거래신고">거래신고</option>
				<option value="서비스기능">서비스 기능</option>
				<option value="광고">광고/전문상점</option>
				<option value="서비스장애">서비스 장애</option>
				<option value="제안">제안</option>
				<option value="기타">기타 신고</option>
			</select>
		
		<div class="search-container" style="float: right;" align="right">
			<input type="text" placeholder="MemberName..." name="search"
				id="qnaSearch">
			<button type="submit" id="qnaMemSearch">
				<i class="fa fa-search"></i>
			</button>
			<!-- <form action="./qnaMemberSearch">
			</form> -->
		</div>
	</div>
	<table class="table">
		<thead>
			<tr>
				<th>상담 카테고리</th>
				<th>문의 제목</th>
				<th>문의자 ID</th>
				<th>첨부파일 여부</th>
				<th>문의 날짜</th>
				<th>문의 상황</th>
				<th>문의 답변날짜</th>
				<th>문의 답변버튼</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${qna_adlist}" var="list" varStatus="i">
				<tr>
					<td>${list.qna_kind}</td>
					<td style="width: 756px;">${list.qna_title}</td>
					<td>${list.mem_id}</td>
					<td><c:if test="${list.fileCheck eq 1}"> O</c:if> <c:if
							test="${list.fileCheck ne 1}"> X</c:if></td>
					<td>${list.qna_regDate}</td>
					<td><c:if test="${list.qna_check eq 0}">답변대기중</c:if> <c:if
							test="${list.qna_check ne 0}">답변완료</c:if></td>
					<td><c:if test="${list.qna_checkDate eq null}">답변대기중</c:if> <c:if
							test="${list.qna_checkDate ne null}">${list.qna_checkDate}</c:if></td>
					<td><c:if test="${list.qna_check eq 0}">
							<a title="qnaAnswer" class="check"><button class="qna_num"
									id="${list.qna_num}">답변하기</button></a>
						</c:if></td>

				</tr>

				<tr >
					<td><button class="more" id="${i.count}">문의내용 확인</button> </td>	<!-- 리스트중 어디를 눌러도 문의내역이 뜰수있게 세팅 -->
																						<!-- 스크립트에서 this를 쓸수 있게 id에 count세팅 -->
					<td>																<!-- 같은 숫자를 문의내역 뒤에 붙여서 각각 다른 id로 설정 -->
					<textarea id="hiddenCont${i.count}" disabled="disabled" class="form-control" style="resize:none; 
					margin: 0px; width: 740px; height: 240px; display: none;" >${list.qna_contents}</textarea>
					</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>

			</c:forEach>
		</tbody>
	</table>

	<script type="text/javascript">
		$(".more").click(function() { // 모든 리스트가 같은 click이벤트를 사용하므로 class식별자로 받아옴
			var i = $(this).attr("id"); // 문의내역은 1,2,3 순으로 증가함 / more클래스의 id도 같은식으로 증가함 / 클릭이벤트가 나왔을때 변수에 id값 넣기
			if ($("#hiddenCont" + i).is(":visible")) { // 실행
				$("#hiddenCont" + i).slideUp();
			} else {
				$("#hiddenCont" + i).slideDown();
			}
		});

	</script>
</div>