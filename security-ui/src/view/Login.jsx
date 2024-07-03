import { useNavigate } from "react-router-dom";
import MainService from "../service/MainService";
import { useState } from "react";

export default function Login() {
    const [error, setError] = useState('')
    const navigate = useNavigate();

    const handleLogin = async (event) => {

        event.preventDefault();

        let registerDetails = {
            "userName": document.getElementById("uname").value,
            "password": document.getElementById("pass").value
        }

        try {
            const userData = await MainService.login(registerDetails)

            if (userData.token) {
                localStorage.setItem('token', userData.token)
                localStorage.setItem('role', userData.role)
                navigate('/content')
            }else{
                setError(userData.message)
            }

        } catch (error) {

            console.log(error)
            setError(error.message)
            setTimeout(()=>{
                setError('');
            }, 5000);
            
        }
    }

    return (
        <>
            <div>
                <input type="text" id="uname"></input>
                <input type="password" id="pass"></input>
            </div>
            <div>
                <button type="submit" onClick={handleLogin}>Login</button>
            </div>
        </>
    )
}