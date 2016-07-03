<!DOCTYPE html>
<html>
<head>
	<title>后台管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" href="/cms/css/admin.css">
    <link rel='stylesheet' href='/cms/css/theme-default.css' type='text/css' media='screen' />
	<script type="text/javascript" charset="UTF-8" src="/cms/script/jquery-1.9.1.min.js"></script>
	
</head>
<body style="zoom: 1;">
	<div class="b-container">
		<#include "../top_menu.ftl"/>
		<div id="modulemenu" >
           <ul class="nav">
           <li data-id="list"><a id="currentItem" href="javascript:showDropMenu('project', '1046', 'project', 'task', 'unclosed')">快速导航 <span class="icon-caret-right"></span></a></li>

           <li  data-id="task"><a href="/cms/user/list">会员</a>
           </li>
           <li  data-id="story"><a href="/cms/user/list">会员列表</a>
           </li>

           </ul>
        </div>
		<div id="wrap">
            <div class="outer with-side with-transition" style="min-height: 600px;">
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
                                        <td>${user.status?default("")}<#if user.status?default("") = "1">  已禁用 <#else> 正常  </#if>  </td>

                                        <td>
                                            <a href="#">重置密码</a><span>|</span>
                                            <#if user.status?default("") = "1">
                                                <a href="javascript:void(0)" onclick="forbiddenUser(${user.id})">恢复用户</a>
                                            <#else>
                                                <a href="javascript:void(0)" onclick="activation(${user.id})">禁用用户</a>
                                            </#if>

                                        </td>
                                    </tr>
                                </#list>
                                </tbody>
                            </table>

                        </div>


                	${pageDiv}
                </div>
            </div>
        </div>

		


		</div>

	</div>

	<script type="text/javascript">
		//DOM加载完毕执行
		$(document).ready(function(){
			$("#left_menu_welcome").addClass("selected");
		});
	</script>


<div style="display: none; position: fixed; left: 0px; top: 0px; width: 100%; height: 100%; cursor: move; opacity: 0; background: rgb(255, 255, 255);"></div></body></html>