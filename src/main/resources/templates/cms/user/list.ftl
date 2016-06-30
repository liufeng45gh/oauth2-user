<!DOCTYPE html>
<html>
<head>
	<title>后台管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" href="/cms/css/admin.css">
	<link rel="shortcut icon" href="/cms/images/favicon.ico">
	<script type="text/javascript" charset="UTF-8" src="/web/script/jquery-1.9.1.min.js"></script>
	
</head>
<body style="zoom: 1;"><div class="" style="display: none; position: absolute;"><div class="aui_outer"><table class="aui_border"><tbody><tr><td class="aui_nw"></td><td class="aui_n"></td><td class="aui_ne"></td></tr><tr><td class="aui_w"></td><td class="aui_c"><div class="aui_inner"><table class="aui_dialog"><tbody><tr><td colspan="2" class="aui_header"><div class="aui_titleBar"><div class="aui_title" style="cursor: move; display: block;"></div><a class="aui_close" href="javascript:/*artDialog*/;" style="display: block;">×</a></div></td></tr><tr><td class="aui_icon" style="display: none;"><div class="aui_iconBg" style="background: none;"></div></td><td class="aui_main" style="width: auto; height: auto;"><div class="aui_content" style="padding: 20px 25px;"></div></td></tr><tr><td colspan="2" class="aui_footer"><div class="aui_buttons" style="display: none;"></div></td></tr></tbody></table></div></td><td class="aui_e"></td></tr><tr><td class="aui_sw"></td><td class="aui_s"></td><td class="aui_se" style="cursor: se-resize;"></td></tr></tbody></table></div></div>
	<div class="container">
		<#include "../top_menu.ftl"/>
		<div id="info_bar" >
			<label class="navindex" style="display:none;"><a href="#">快速导航管理</a></label>
			<span class="nav_sec"></span>
		</div>
		<#include "left_menu.ftl"/>
		

		<div id="admin_right">
			<div class="content_box" style="border:none">
			<div class="position"><span>会员</span><span>&gt;</span><span>会员管理</span><span>&gt;</span><span>会员列表</span></div>
			<div class="operating">
				<div class="search f_l">
					
					<form  action="" method="get">
						账号
						<input class="small" name="account" type="text" value="">
						昵称
						<input class="small" name="nick" type="text" value="">
						状态
						<select class="auto" name="status">
							<option value="0">正常</option>
							<option value="1">禁用</option>
						</select>
						角色
						<select class="auto" name="role">
							<option value="admin">管理员</option>
							<option value="user">普通账号</option>
						</select>
						<button class="btn" type="submit"><span class="sch">搜 索</span></button>
					</form>
				</div>				
			</div>
	<div class="content" style="min-height: 200px;">			
		<table class="list_table" style="font-size:14px;">		
			<thead>
				<tr style="height:30px;">
					<th width="140px">id</th>
					<th width="340px">账号</th>
					<th width="150px">昵称</th>
					<th width="180px">角色</th>
					<th width="180px">状态</th>
					
					<th>操作</th>
				</tr>
			</thead>			
			<tbody>
			<#list userList as user>
				<tr>
					<td>${user.id}</td>
					
					<td>${user.account}</td>
					
					<td>${user.nickName}</td>
					<td>${user.role?default("普通用户")}</td>
					<td>${user.status?default("")}</td>
					
					<td>
						<a href="#">修改</a>
						<a href="javascript:void(0)" onclick="delModel({link:'/iwebshop/index.php?controller=goods&amp;action=goods_del&amp;id=1'})">删除</a>
					</td>
				</tr>
			</#list>
			</tbody>
		</table>
		
	</div>


	${pageDiv}
</div>
		</div>
		<div id="separator"></div>
	</div>

	<script type="text/javascript">
		//DOM加载完毕执行
		$(document).ready(function(){
			$("#left_menu_welcome").addClass("selected");
		});
	</script>


<div style="display: none; position: fixed; left: 0px; top: 0px; width: 100%; height: 100%; cursor: move; opacity: 0; background: rgb(255, 255, 255);"></div></body></html>