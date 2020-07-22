<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="w3-container">
	<div class="topnav">
		<a class="check active" title="NoticeList">공지 목록</a>
		<a class="check" title="NoticeWrite">공지 등록</a>
		<div class="search-container">
			<input type="text" placeholder="Notice Search.." name="search"
				id="noticeSearch">
			<button type="submit" id="noticeTitleSearch">
				<i class="fa fa-search"></i>
			</button>
		</div>
	</div>
	<table class="table">
		<thead>
			<tr>
				<th>글 번호</th>
				<th>공지 제목</th>
				<th>공지 게시일</th>
				<th>공지 삭제</th>
				<th>공지 수정</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="li">
				<tr>
					<td>${li.nonum}</td>
					<td><a href="#" title="NoticeSelect" class="check pointCursor"
						id="${li.nonum}">${li.notitle}</a></td>
					<td>${li.no_regDate}</td>
					<td width="50px"><button id="noticeDel" title="${li.nonum}"
							class="btn btn-danger">공지 삭제</button></td>
					<td width="50px"><button id="noticeUpd" title="${li.nonum}"
							class="btn btn-success">공지 수정</button></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>