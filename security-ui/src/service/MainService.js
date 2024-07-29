
class MainService {
    static BASE_API_URL = "http://localhost:8090";

    static async login(loginDetails) {
        console.log(loginDetails)
        try {
            const response = await fetch(`${this.BASE_API_URL}/login`, {
                method: "POST",
                body: JSON.stringify(loginDetails),
                headers: {
                    "Content-Type": "application/json",
                }
            });

            const userData = await response.json(); // Wait for JSON parsing
            return userData;
        } catch (err) {

            console.error("Error during login:", err);

        }
    }


    static async register(registerDetails) {

        try {
            const response = await fetch(`${this.BASE_API_URL}/register/user`, {
                method: "POST",
                body: JSON.stringify(registerDetails),
                headers: {
                    "Content-Type": "application/json",
                }
            });

            const userData = await response.json(); // Wait for JSON parsing
            return userData;
        } catch (error) {

        }

    }

    static async logout() {

        try {
            const token = localStorage.getItem('token')
            const response = await fetch(`${this.BASE_API_URL}/logout`, {
                method: "POST",
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }

            })

        }
        catch (error) {
            console.error('Logout error:', error);
        }
        finally {
            localStorage.removeItem('token');
            localStorage.removeItem('role');
        }

    }


    static isAuthenticated() {
        const token = localStorage.getItem('token')
        return !!token
    }


    static isAdmin() {
        const role = localStorage.getItem('role')
        return role === 'ADMIN'
    }

    static isUser() {
        const role = localStorage.getItem('role')
        return role === 'USER'
    }

    static adminOnly() {
        return this.isAuthenticated() && this.isAdmin();
    }


}

export default MainService;