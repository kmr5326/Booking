import logo from "./logo.svg";
import "./App.css";
import "./index.css";

function App() {
  return (
      <div className="App bg-[#12BD7E] h-screen overflow-hidden flex text-black">
    <img src="logo.png" alt="Logo" className="h-24 mb-4 ml-4" />
  <div className="flex flex-col justify-center items-center w-1/2 h-full p-8">
    <img src="qrcode_play.google.com.png" alt="QR Code" className="h-80 w-80 mb-4" />
  </div>
  
  <div className="flex flex-col items-start w-1/2 h-full p-8 justify-center">
    <h1 className="text-7xl font-extrabold mb-10">북킹</h1>
    <div className="ml-3">
    <div className="text-2xl text-start">북킹은 사용자의 위치를 기반으로 <br/>주변의 독서 모임을 검색, 참여, 관리까지 </div>
    <div className="mb-4 text-2xl text-start">하나의 앱에서 제공하는 서비스입니다.</div>
    <div className="">
     <a href="https://play.google.com/store/apps/details?id=com.ssafy.booking" target="_blank">
  <img src="google-play-badge.png" alt="Google Play" className="h-24 mr-4" />
</a>
</div>
    </div>
   
  </div>
  
  <a
    href="https://zep.us/play/yOZ9WR"
    target="_blank"
    className="absolute top-4 right-4"
  >
    <img src="symbol_wordmark_light.png" alt="Link" className="h-12" />
  </a>
</div>

  );
}

export default App;
