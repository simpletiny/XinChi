// var min = 5;
// var sec = 0;
// function TimeDown() {
//
// if (sec == 0) {
// min--;
// sec = 60;
// }
// sec--;
//
// var str = ((min < 10) ? "0" + min : min) + ":"
// + ((sec < 10) ? "0" + sec : sec);
//
// $("#reboot-timer").text(str)
// }
//
// // 将消息显示在网页上
//
// function setMessageInnerHTML(innerHTML) {
//
// document.getElementById('message').innerHTML += innerHTML + '<br/>';
//
// }
//
// // 关闭WebSocket连接
//
// function closeWebSocket() {
//
// websocket.close();
//
// }
//
// // 发送消息
//
// function send() {
//
// var message = document.getElementById('text').value;
// console.log(message);
// websocket.send(message);
//
// }
// var websocket = null;
// $(document).ready(function() {
// setInterval('TimeDown()', 1000)
// // 判断当前浏览器是否支持WebSocket
//
// if ('WebSocket' in window) {
// websocket = new WebSocket("ws://localhost:8080/XinChi/ws/websocket");
// } else {
// alert('当前浏览器 Not support websocket')
// }
//
// // 连接发生错误的回调方法
//
// websocket.onerror = function() {
//
// };
//
// // 连接成功建立的回调方法
//
// websocket.onopen = function() {
//
// }
//
// // 接收到消息的回调方法
//
// websocket.onmessage = function(event) {
//
// setMessageInnerHTML(event.data);
//
// }
//
// // 连接关闭的回调方法
//
// websocket.onclose = function() {
//
// }
//
// // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
//
// window.onbeforeunload = function() {
// closeWebSocket();
// }
//
// });
function click_menu(li) {
	// console.log(li);
	// console.log("test");
}