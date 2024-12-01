import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>

      <div>
        <label>Username : </label>
        <input type='text' name='uid'></input>  </div>

      <div>
        <label>Password : </label>
        <input type='password' name='pass'></input>
      </div>

      <div>
        <button type='submit'> Login </button>
        <button type='submit'> Register </button>
      </div>
    </>
  )
}

export default App
