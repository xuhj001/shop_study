<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">


<title>评论管理</title>
<meta name="keywords" content="H+后台主题,后台bootstrap框架,会员中心主题,后台HTML,响应式后台">
<meta name="description"
	content="H+是一个完全响应式，基于Bootstrap3最新版本开发的扁平化主题，她采用了主流的左右两栏式布局，使用了Html5+CSS3等现代技术">

<link rel="shortcut icon" href="favicon.ico">
<link href="/css/bootstrap.min14ed.css?v=3.3.6" rel="stylesheet">
<link href="/css/font-awesome.min93e3.css?v=4.4.0" rel="stylesheet">

<link href="/css/animate.min.css" rel="stylesheet">
<link href="/css/style.min862f.css?v=4.1.0" rel="stylesheet">

</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content  animated fadeInRight">

		<div class="row">
			<c:forEach items="${list}" var="item">
				<div class="col-sm-6">

					<div class="social-feed-box">

						<div class="social-avatar">
							<a href="#" class="pull-left"> <img alt="image"
								<c:if test="${user.img==null}">src="/img/hu.jpg"</c:if>
								<c:if test="${user.img!=null}">src="${user.img}"</c:if>>
							</a>
							<div class="media-body">${item.title}</div>
						</div>


						<div class="social-body">
							<p>卖点</p>
							<img src="${item.itemPic}" class="img-responsive">
						</div>
						<div class="social-footer">

							<c:forEach items="${item.userlist}" var="userlist"
								varStatus="parentStatus">

								<div class="social-comment">

									<a href="#" class="pull-left"> <img alt="image"
										<c:if test="${userlist.pic==null}">src="/img/a1.jpg"</c:if>
										<c:if test="${userlist.pic!=null}">src="${userlist.pic}"</c:if>
										name="pic">
									</a>

									<div class="media-body">
										<label>${userlist.buyername}</label> <br />${userlist.comments}<br />
										<small class="text-muted"><fmt:formatDate
												value="${userlist.time}" pattern="yyyy-MM-dd HH:mm:ss" /></small>
									</div>
									<!--第一层回复-->
									<div class="social-comment comments">
										<a href="javascript:void(0)" class="replya"
											id="${parentStatus.index}-parent">回复</a>
									</div>
									<!--第一层评论的评论框-->
									<div
										class="social-comment parent-comments ${parentStatus.index}"
										style="display: none;">
										<form action="/page/create/comments" method="post"
											class="${parentStatus.index}">
											<div class="media-body reply">
												<!--商品id-->
												<input type="hidden" value="${userlist.itemid}"
													name="itemId" />
												<!--该用户id-->
												<input type="hidden" value="${userlist.id}" name="parentid" />
												<input type="hidden" value="${userlist.buyername}"
													name="replyname" />
												<textarea name="replycomments" class="form-control textarea"
													placeholder="填写评论..." name="replycomments"></textarea>
												<input class="form-control submit "
													id="${parentStatus.index}" type="button"
													onclick="submitParentForm(this)" value="发表"></input> <input
													type="hidden" value="${userlist.muserId}" name="replyid"
													class="replyid" />
											</div>
										</form>
									</div>
									<!--评论下的回复-->
									<c:forEach items="${userlist.replylist}" var="replylist"
										varStatus="childStatus">

										<div class="social-comment">
											<a href="#" class="pull-left"> <img alt="image"
												<c:if test="${replylist.pic==null}">src="${user.img}"</c:if>
												<c:if test="${replylist.pic!=null}">src="${replylist.pic}"</c:if>
												name="pic">
											</a>
											<div class="media-body">
												<label name="replyname">${replylist.name}</label> <br />
												回复${replylist.replyname}: <br />${replylist.replycomments}
												<br /> <small class="text-muted"><fmt:formatDate
														value="${replylist.replytime}"
														pattern="yyyy-MM-dd HH:mm:ss" /></small>
											</div>
										</div>
										<div class="social-comment comments">
											<!--第二层回复-->
											<c:if test="${replylist.userid!=user.id}">
												<a href="javascript:void(0)" class="replya"
													id="${childStatus.index}-child">回复</a>
											</c:if>
											<c:if test="${replylist.userid==user.id}">
												<a href="/page/delete/comments?id=${replylist.id}"
													class="replya" id="${childStatus.index}-child">删除</a>
											</c:if>
										</div>
										<!--第二层评论的评论框-->
										<div
											class="social-comment child-comments ${childStatus.index}"
											style="display: none;">
											<form action="/page/create/comments" method="post"
												class="${childStatus.index}}">
												<!--商品id-->
												<input type="hidden" value="${userlist.itemid}"
													name="itemId" />
												<!--该用户id-->
												<input type="hidden" value="${userlist.id}" name="parentid" />
												<input type="hidden" value="${replylist.name}"
													name="replyname" />
												<div class="media-body reply">
													<textarea class="form-control textarea"
														placeholder="填写评论..." name="replycomments"></textarea>
													<input class="form-control submit"
														name="${childStatus.index}" type="submit"
														onclick="submitChildForm(this)" value="发表" />
													<!--被回复用户id-->
													<input type="hidden" value="${replylist.replyid}"
														name="replyid" class="replyid" />
												</div>
											</form>
										</div>

									</c:forEach>

								</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
	<script src="/js/jquery.min.js?v=2.1.4"></script>
	<script src="/js/bootstrap.min.js?v=3.3.6"></script>
	<script src="/js/content.min.js?v=1.0.0"></script>
	<script>
		//提交函数
		//第一层评论
		function submitParentForm(obj) {
			//id代表是哪个表单进行提交
			var id = obj.id;
			// 通过 form 的 id 取得 form
			var $form = $("form[class=" + id + "]");
			$form.submit();
			//获取兄弟元素，内有被回复的用户id
			var input = $form.find("input.replyid");
			parent.ws.send(input.val() + ",回复");
		}

		//第二层评论
		function submitChildForm(obj) {
			//id代表是哪个表单进行提交
			var id = obj.id;
			// 通过 form 的 id 取得 form
			var $form = $("form[class=" + id + "]");
			$form.submit();
			// 通知websocket
			var input = $form.find("input.replyid");
			parent.ws.send(input.val() + ",回复");
		}
		$(document).ready(function() {
			$(".submit").click(function() {
				//获取评论框的内容
				var text = $(".textarea").text();
				if (text == null)
					alert("回复内容不能为空");
			});

			//监听回复标签的点击
			$(".replya").click(function() {
				//获取点击处的id(索引值)，便于筛选

				var id = $(this).attr("id");
				var indexId = id.substring(0, id.indexOf("-"));//截取出index
				//判断是第一层还是第二层评论
				var parent = "parent";
				if (id.indexOf(parent) >= 0) {
					//第一层
					//循环第一层元素
					$("div .parent-comments").each(function(index, element) {
						var div = $(this);
						var classname = $(this).attr("class");
						if (classname.indexOf(indexId) >= 0) {
							div.toggle();
							return false;
						}

					});
				} else {
					//第二层
					$("div .child-comments").each(function(index, element) {
						var classname = $(this).attr("class");
						if (classname.indexOf(indexId) >= 0) {
							$(this).toggle();
							return false;
						}

					});
				}

			});

			//发表按钮提交
		});
	</script>
	<script type="text/javascript"
		src="http://tajs.qq.com/stats?sId=9051096" charset="UTF-8"></script>
</body>


<!-- Mirrored from www.zi-han.net/theme/hplus/social_feed.html by HTTrack Website Copier/3.x [XR&CO'2014], Wed, 20 Jan 2016 14:19:44 GMT -->
</html>