import axios from 'axios'
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'

const JoinPage = () => {
  const navigate = useNavigate()
  const [formData, setFormData] = useState({
    username: '더미이름',
    email: 'dumy@hot.com',
    password: '123',
    role: 'ROLE_USER'
  })
  //사용자가 입력한 4가지 정보 초기화 하기
  const handleChange = (event) => {
    const { id, value } = event.target 
    //const { name, value} = event.target 
    setFormData({
      ...formData,
      [id]:value
    })
  }//end of handleChange
  //사용자가 4가지 정보를 입력한 뒤 회원가입 버튼을 누르면
  //-> http://localhost:8000/member/memberInsert
  const handleSignup = async () => {
    try{
      const response = await axios.post(`${import.meta.env.VITE_SPRING_IP}member/memberInsert`, formData)
      //서버에서 응답으로 받는 값이 1이면 등록 성공, 0이면 등록 실패
      console.log(response)
      navigate('/')
    }catch(error){
      console.error("회원가입 실패", error)
    }
  }//end of handleSignup

  return (
    <>
      <div className="row my-5 justify-content-center">
        <div className="col-8 col-md-6 col-lg-4">
          <h3 className="text-center mb-5">회원가입</h3>
          <form className="frm">
          <div className="input-group my-2">
              <div className="input-group-text">이 름</div>
              <input 
                  className="form-control" 
                  id='username'
                  name="username" 
                  type="text"
                  placeholder="이름을 입력해주세요"
                  value={formData.username}
                  onChange={handleChange}
              />
          </div>
          <div className="input-group my-2">
              <div className="input-group-text">이 메 일</div>
              <input 
                  className="form-control" 
                  id='email'
                  name="email" 
                  type='text'
                  value={formData.email}
                  onChange={handleChange}
              />
          </div>
          <div className="input-group my-2">
              <div className="input-group-text">비밀번호</div>
              <input
                  className="form-control"
                  id='password'
                  name="password"
                  type="password"
                  value={formData.password}
                  onChange={handleChange}
              />
          </div>
          <div className="input-group my-2">
              <div className="input-group-text">권 한</div>
              <select 
                  className="form-select"
                  id='role' 
                  name="role"
                  value={formData.role}
                  onChange={handleChange}
              >
                  <option value="ROLE_USER">ROLE_USER</option>
                  <option value="ROLE_MANAGER">ROLE_MANAGER</option>
                  <option value="ROLE_ADMIN">ROLE_ADMIN</option>
              </select>
          </div>
          <div className="my-3">
              <button className="btn btn-success w-100" type="button" onClick={handleSignup}>회원가입</button>
          </div>
          <div className="text-end mt-3"><a href="/login">로그인</a></div>
          </form>
        </div>
      </div>
    </>
  )
}

export default JoinPage
