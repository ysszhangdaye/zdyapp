/**
 * XX管理 js
 * 
 */

var totalPage = 1;
var pageSize = 10;

$(function() {
	
	$("#addBtn").click(function() {
		$(".error").remove();
		$("#edit-frm")[0].reset();
		$("#edit-frm").find("#editFlag").val("add");
		$('#editModal').modal('show');
	});
	
	$("#checkall").click(function(){
		$("#datatable").find(":checkbox").prop("checked", $(this).prop("checked"));
	});
	
	editDataValidate();
	
	queryList(totalPage, pageSize, true);
	
	$("#deleteBtn").click(function(){
		deleteBatch();
	});
	
	$("#edit-frm").find("#type").change(function(){
		typeChange($(this).val());
	});
	
});


function checkboxBind(){
	$("#datatable").find(":checkbox[name='datacheckbox']").unbind().click(function(){
		if($("#datatable").find(":checkbox[name='datacheckbox']:checked").length
				== $("#datatable").find(":checkbox[name='datacheckbox']").length){
			$("#checkall").prop("checked", true);
		}else{
			$("#checkall").prop("checked", false);
		}
	});
}


//initFlag false/true不/重新初始化分页信息
function queryList(curPage, pageSize, initFlag) {
	$.get("links/findlinkslist", {
		"page" : curPage,
		"pageSize" : pageSize,
		"linksName" : $("#search-frm").find("#linksName").val()
	},
	function(data) {
		if (data.success == true) {
			var totalPage = data.data.totalPage;
			if (data.data.maxPage == 0) {
				totalPage = 1;
			}
			if (initFlag) {
				$.jqPaginator('#pagetable',{
					totalPages : totalPage,
					visiblePages : pageSize,
					currentPage : 1,
					prev : '<li class="prev"><a href="javascript:;">上页</a></li>',
					next : '<li class="next"><a href="javascript:;">下页</a></li>',
					first : '<li class="first"><a href="javascript:;">首页</a></li>',
					last : '<li class="last"><a href="javascript:;">尾页</a></li>',
					page : '<li class="page"><a href="javascript:;">{{page}}</a></li>',
					onPageChange : function(
							num, type) {
						if ((this.currentPage != 1) || (1 != num)) {
							queryList(num, pageSize, false);
						}
					}
				});

			}
			insertTable(data.data.list);

		} else {
			alert(data.msg);
		}

	}, "json");

}

function insertTable(data){//#datatable
	$("#datatable").find("tbody").empty();
	if(data.length > 0){
		var content = "";
		$.each(data, function(index, item){
			content += "<tr><td><input type='checkbox' value='" + item.id + "' name='datacheckbox' ></td>";
			content += "<td>" + item.linkName + "</td>";
			content += "<td><a target='_blank' href=\""+item.linkAddr+"\">" + item.linkAddr + "</a></td>";
			content += "<td>" + chooseValue({0:'显示', 1:'不显示'}, item.display) + "</td>";
			content += "<td>" + chooseValue({0:'友情链接', 1:'合作网站'}, item.linkType) + "</td>";
			content += "<td>" +  item.linkComment + "</td>";
			content += "<td>" + ObjectUtil.fmtTime(item.createTime, "yyyy-mm-dd HH:mm:ss")+ "</td>";
			content += "<td>" +  ObjectUtil.fmtTime(item.operTime, "yyyy-mm-dd HH:mm:ss") + "</td>";
			content += "<td class=\"actions\" ><a href=\"javascript:showModify('" + item.id + "')\" class=\"on-default edit-row\" title=\"编辑\"><i class=\"fa fa-pencil\">&nbsp;编辑</i></a>";
			content += "<a href=\"javascript:deleteData('" + item.id + "')\" class=\"on-default remove-row\"><i class=\"fa fa-trash-o\" title=\"删除\">&nbsp;删除</i></a>";
			content += "</td></tr>";
		});
		$("#datatable").find("tbody").append(content);
	}
	checkboxBind();
}


function editlinks(){
	var url = "links/addlinks";
	if($("#edit-frm").find("#editFlag").val() == "modify"){
		url = "links/modifylinks";
	}
	$.ajax({
		url : url,
		type : 'post',
		async : false,
		data : $("#edit-frm").serialize(),
		dataType : 'json',
		success : function(result) {
			if (result.success) {
				alert(result.msg);
				$('#editModal').modal('hide');
			} else {
				alert(result.msg);
			}
			queryList(1, pageSize, true);
		}
	});
}


function showModify(id){
	$(".error").remove();
	var data;
	$.ajax({
		url:'links/getlinks',
		type:'post',
		data:{id:id},
		async:false,
		dataType:"json",
		success:function(result){
			if(result.success){
				data = result.data;
			}else{
				alert(result.msg);
			}
		}
	});
	$("#edit-frm")[0].reset();
	$("#edit-frm").find("#editFlag").val("modify");
	$("#edit-frm").find("#id").val(data.id);
	$("#edit-frm").find("#linkName").val(data.linkName);
	$("#edit-frm").find("#linkAddr").val(data.linkAddr);
	$("#edit-frm").find("input[name='display'][value='"+data.display+"']").prop("checked", true);
	$("#edit-frm").find("input[name='linkType'][value='"+data.linkType+"']").prop("checked", true);
	$("#edit-frm").find("#linkComment").val(data.linkComment);
	$('#editModal').modal('show');
	
}
function deleteBatch(){
	var obj = $("#datatable").find(":checkbox[name='datacheckbox']:checked");
	if(obj.length > 0){
		var ids = "";
		$.each(obj, function(index, item){
			ids += $(this).val() + ",";
		})
		ids = ids.substring(0, ids.length-1);
		deleteData(ids);
	}else{
		alert("请选择要删除的信息！！！");
	}
}

function deleteData(id){
	if(confirm("确认删除该条信息吗？")){
		$.ajax({
			url:'links/deletelinks',
			type:'post',
			data:{id:id},
			dataType:"json",
			success:function(result){
				if(result.success){
					alert(result.msg);
				}else{
					alert(result.msg);
				}
				queryList(1, pageSize, true);
			}
		});
	}
}

function editDataValidate(){
	$("#edit-frm").validate(
			{
				submitHandler : function(form) {
					editlinks();
				},
				rules : {
					linkName:{
						required : true
					},
					linkAddr:{
						required : true,
						url2 : true
					},
					linkComment:{
						required : true
					}
					
				},
				messages : {
					linkName:{
						required : "请输入链接名称"
					},
					linkAddr:{
						required : "请输入链接地址",
						url2 : "请输入正确的连接地址"
					},
					linkComment:{
						required : "请输入链接说明"
					}
				},
				errorElement : "em",
				errorPlacement : function(error, element) {
					error.addClass("help-block");
					if (element.prop("type") === "checkbox") {
						error.insertAfter(element.parent("label"));
					} else {
						error.insertAfter(element);
					}
				},
				highlight : function(element, errorClass, validClass) {
					$(element).parents(".col-sm-5").addClass("has-error")
							.removeClass("has-success");
				},
				unhighlight : function(element, errorClass, validClass) {
					$(element).parents(".col-sm-5").addClass("has-success")
							.removeClass("has-error");
				}

			});
}


