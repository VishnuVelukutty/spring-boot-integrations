class MainService {
    static BASE_API_URL = "http://localhost:8090";

    static async login(details) {
        console.log("Details >>> "+details )
        try {
            const response = await fetch(`${this.BASE_API_URL}/login`, {
                method: "POST",
                body: JSON.stringify(details),
                headers: {
                    "Content-Type": "application/json",
                }
            });
            return response;
        } catch (err) {

            console.error("Error during login:", err);

        }
    }


    static async register(){

    }

    static logout(){
        localStorage.removeItem('token')
        localStorage.removeItem('role')
    }


    static isAuthenticated(){
        const token = localStorage.getItem('token')
        return !!token
    }


    static isAdmin(){
        const role = localStorage.getItem('role')
        return role === 'ADMIN'
    }

    static isUser(){
        const role = localStorage.getItem('role')
        return role === 'USER'
    }

    static adminOnly(){
        return this.isAuthenticated() && this.isAdmin();
    }


}

export default MainService;