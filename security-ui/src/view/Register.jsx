import { useState } from "react";
import { useNavigate } from "react-router-dom";
import MainService from "../service/MainService";

export default function Register() {
    const navigate = useNavigate();
    const [error, setError] = useState(null);
    const [selectValue, setSelectValue] = useState(null); // Default to null

    const handleLogin = () => {
        navigate('/');
    }

    const handleRegister = async (event) => {
        event.preventDefault();
        const uname = event.target.elements.uname.value;
        const pass = event.target.elements.pass.value;

        const registerDetails = {
            userName: uname,
            userPass: pass,
            userRole: selectValue // This can be null or a selected role value
        };

        try {
            const registerData = await MainService.register(registerDetails);
            // Optionally, handle success here (e.g., show success message, redirect)
            console.log("Registration successful:", registerData);
        } catch (error) {
            console.error("Registration error:", error);
            setError(error.message);
            setTimeout(() => {
                setError(null);
            }, 5000);
        }
    }

    const handleRoleChange = (event) => {
        setSelectValue(event.target.value);
    }

    return (
        <>
            <div>
                <form onSubmit={handleRegister}>
                    <div>
                        Username: <input type="text" name="uname" required />
                    </div>
                    <div>
                        Password: <input type="password" name="pass" required />
                    </div>
                    <div>
                        Role: 
                        <select id="role" name="role" value={selectValue || ''} onChange={handleRoleChange}>
                            <option value="">Select Role</option>
                            <option value="USER">USER</option>
                            <option value="ADMIN">ADMIN</option>
                        </select>
                    </div>

                    <div>
                        <button type="submit">Register</button>
                        <button type="button" onClick={handleLogin}>Main Page</button>
                    </div>
                </form>
            </div>
            {error && <div>Error: {error}</div>}
        </>
    );
}
