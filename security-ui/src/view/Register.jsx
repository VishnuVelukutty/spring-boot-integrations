import { useNavigate } from "react-router-dom";


export default function Register() {

    const navigate = useNavigate();


    const handleLogin = () => {
        navigate('/')
    }

    const handleRegister = () =>{
        
    }



    return (
        <>
            <div>
                First Name : <input type="text" id="fname"></input>
                Last Name : <input type="text" id="lname"></input>
                Username : <input type="text" id="uname"></input>
                Password : <input type="password" id="pass"></input>
            </div>
            <div>
                <button type="submit" onClick={handleLogin}>Login Page</button>
            </div>
            <div>
                <button type="submit" onClick={handleRegister}>Register</button>
            </div>
        </>
    )
}