import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import ParticlesBG from "./components/ParticlesBG";



function App() {
    return (
        <div className="">
            <BrowserRouter>
                <div className="bg-wrapper">
                    <ParticlesBG/>
                </div>
                <Navbar/>
                <div className="page-wrapper container">
                    <Routes>
                        <Route exact path="/"
                               element={<Home/>}
                        />
                    </Routes>
                </div>
            </BrowserRouter>
        </div>
    );
}

export default App;
