<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html; charset=gb2312" language="java"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="css.css" rel="stylesheet" type="text/css">
<title>�û���¼</title>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function checkForm(){
		if(document.all.userName.value.length < 1 || document.all.password.value.length < 1){
			alert("��¼�ʺź����붼Ҫ����");
			return false;
		}
		form1.action = "/JFramework/login.in";
		form1.submit();
	}
	function getFocus(){
		if(document.all.userName.value > 1){
			document.all.password.focus();
		}else{
			document.all.userName.focus();
		}
	}
	window.onload = getFocus;
//-->
</SCRIPT>
</head>

<body onload="getFocus()">
<form name="form1" method="post" action="" onkeypress="if(event.keyCode==13)this.loginButton.click();">
  <table width="101%"  border="1" class="contentTable1">
    <tr bgcolor="#EBEBEB">
      <td colspan="2" class="title">�û���¼</td>
    </tr>
    <tr>
      <td bgcolor="#EBEBEB"><div align="right">��¼�ʺ�</div></td>
      <td width="60%">
      	<input name="userName" type="text" class="title0" id="userName" value="jinpj"/>
      </td>
    </tr>
    <tr>
      <td bgcolor="#EBEBEB"><div align="right">�ܡ���</div></td>
      <td width="60%">
      	<input name="password" type="password" class="title0" id="password" value="121212">
	  </td>
    </tr>
    <tr>
      <td colspan="2">
      <div align="center">
        <input name="Submit" type="button" class="input_button" value="�� ¼" onclick="checkForm()" id="loginButton"/>
        <input name="" type="reset" class="input_button" value="�� д"/> 
      </div></td>
    </tr>
  </table>
</form>
</body>
</html>
