import React, { useState } from 'react'
import { DividerDiv, DividerHr, DividerSpan, GoogleButton, KakaoButton, LoginForm, MyH1, MyInput, MyLabel, MyP, PwEye, SubmitButton } from '../styles/FormStyles'
import { Link, useNavigate } from 'react-router-dom'
import axios from 'axios'

const LoginView = () => {
    const navigate = useNavigate()
    const [tempUser, setTempUser] = useState({
        email: '',
        password:''
    })
    const changeUser = (e) => {
        const id = e.currentTarget.id 
        const value = e.target.value 
        setTempUser({...tempUser, [id]: value})
    }
    //동일한 input type=text를 이메일인 경우에는 입력값을 노출하고 비번일 때는 히든 처리해야 함.
    const [passwordType, setPasswordType] = useState({
        type: 'password', 
        visible: false
    })
    const loginG = async() => {
        console.log('구글 로그인');
        const googleUrl = "https://accounts.google.com/o/oauth2/v2/auth"
        const googleClientId = `${import.meta.env.VITE_GOOGLE_CLIENTID}`
        //구글서버에서 요청(인가코드보내줘)을 듣고 응답으로 보내줄 URL 미리 등록해 둠
        //구글서버에게 요청을 하게 되면 응답페이지 처리에 대한 제어권이 구글에게 넘어감
        //그래서 미리 응답을 받을 수 있도록 redirecturl을 통해서 쿼리스트링으로
        //인가코드를 넘겨준다.
        //인가코드를 받아서(5173번) 스프링 부트 서버(8000번)에 요청을 전달함.
        const googleRedirectUrl = "http://localhost:5173/oauth/google/redirect"
        const googleScope = "openid profile email"
        try {
            const auth_uri = `${googleUrl}?client_id=${googleClientId}&redirect_uri=${googleRedirectUrl}&response_type=code&scope=${googleScope}`
            console.log(auth_uri);
            // 브라우저 없이 구글 서버에 요청하기
            window.location.href=auth_uri
        } catch (error) {
            console.error("구글 로그인 실패!!!", error);
        }


    }
    const loginK = async () => {
        console.log('카카오로그인');
        const kakaoUrl = "https://kauth.kakao.com/oauth/authorize"
        const kakaoClientId = `${import.meta.env.VITE_KAKAO_CLIENTID}` //rest api key
        const kakaoRedirectUrl = "http://localhost:5173/oauth/kakao/redirect"
        try{
            const auth_uri = `${kakaoUrl}?client_id=${kakaoClientId}&redirect_uri=${kakaoRedirectUrl}&response_type=code`
            window.location.href = auth_uri
        }catch(error){
            console.error("카카오 인증코드 가져오기 실패", error)
        }
    };
    const passwordView =(e) => {
        const id = e.currentTarget.id 
        if(id === "password"){
            if(!passwordType.visible){
                //<input type=text />
                setPasswordType({...passwordType, type:'text', visible: true})
            }else{
                //<input type=password />
                setPasswordType({...passwordType, type:'password', visible: false})
            }
        }
    }
    const [submitBtn, setSubmitBtn] = useState({
        disabled: true, 
        bgColor: 'rgb(175, 210, 244)',
        hover: false
    })    
    const toggleHover =() => {
        if(submitBtn.hover){
            setSubmitBtn({...submitBtn, hover:false, bgColor:'rgb(105,175,245)'})
        }else{
            setSubmitBtn({...submitBtn, hover:true, bgColor:'rgb(58,129,200)'})
        }
    }    
    const loginE = async() => {
        try {
            const response = await axios.post(`${import.meta.env.VITE_SPRING_IP}auth/signin`, tempUser)
            console.log(response.data)
            window.localStorage.setItem("id", response.data.id)
            window.localStorage.setItem("accessToken", response.data.accessToken)
            window.localStorage.setItem("refreshToken", response.data.refreshToken)
            window.localStorage.setItem("username", response.data.username)
            window.localStorage.setItem("email", response.data.email)
            navigate("/home")
        } catch (error) {
            console.error("로그인 실패", error)
        }
    }    
  return (
    <>
        <LoginForm>
        <MyH1>로그인</MyH1>
        <MyLabel htmlFor="email"> 이메일     
            <MyInput type="email" id="email" name="email" placeholder="이메일를 입력해주세요." 
            onChange={(e)=>changeUser(e)}/>   
        </MyLabel>
        <MyLabel htmlFor="password"> 비밀번호
            <MyInput type={passwordType.type} autoComplete="off" id="password" name="password" placeholder="비밀번호를 입력해주세요."
            onChange={(e)=>changeUser(e)}/>
            <div id="password" onClick={(e)=> {passwordView(e)}} style={{color: `${passwordType.visible?"gray":"lightgray"}`}}>
            <PwEye className="fa fa-eye fa-lg"></PwEye>
            </div>
        </MyLabel>
        <SubmitButton type="button" style={{backgroundColor:submitBtn.bgColor}}  
            onMouseEnter={toggleHover} onMouseLeave={toggleHover} onClick={loginE}>
            로그인
        </SubmitButton>
        <DividerDiv>
            <DividerHr />
            <DividerSpan>또는</DividerSpan>
        </DividerDiv>
        <GoogleButton type="button" onClick={loginG}>
            <i className= "fab fa-google-plus-g" style={{color: "red", fontSize: "18px"}}></i>&nbsp;&nbsp;Google 로그인
        </GoogleButton>
        <KakaoButton type="button" onClick={loginK}>
            <span style={{color: "red", fontSize: "18px"}}></span>&nbsp;&nbsp;Kakao 로그인
        </KakaoButton>
        <MyP style={{marginTop:"10px"}}>신규 사용자이신가요?&nbsp;<Link to="/joinForm" className="text-decoration-none" style={{color: "blue"}}>계정 만들기</Link></MyP>
        <MyP>이메일를 잊으셨나요?&nbsp;<Link to="/login/findEmail" className="text-decoration-none" style={{color: "blue"}}>이메일 찾기</Link></MyP>
        <MyP>비밀번호를 잊으셨나요?&nbsp;<Link to="/login/resetPwd" className="text-decoration-none" style={{color: "blue"}}>비밀번호 변경</Link></MyP>
        </LoginForm>      
    </>
  )
}

export default LoginView