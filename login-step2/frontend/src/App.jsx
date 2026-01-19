import "bootstrap/dist/css/bootstrap.min.css"
import { Route, Routes, useNavigate } from "react-router-dom"
import HomePage from "./components/pages/HomePage"
import BoardPage from "./components/pages/BoardPage"
import IsTokenExpiration from "./components/auth/IsTokenExpiration"
import { useEffect, useState } from "react"

function App() {
  const navigate = useNavigate()
  //token상태를 관리하기 위해서 useState로 변경하기
  const [token, setToken] = useState(()=>{
    const token = window.localStorage.getItem('accessToken')
    return token
  })
  //TODO - 토큰 유효시간 체크 - 파기/유지
  const isTokenExpire = IsTokenExpiration(token)
  console.log(isTokenExpire)// true이면 만료, false이면 유효
  useEffect(() => {
    if(isTokenExpire){
      window.localStorage.clear()
      navigate("/", {replace: true})
    }
  },[token, isTokenExpire, navigate])
  return (
    <>
      <Routes>
        <Route path="/home" element={<HomePage />} />
        <Route path="/board" element={<BoardPage />} />
      </Routes>
    </>
  )
}

export default App
