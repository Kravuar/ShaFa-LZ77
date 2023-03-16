import React from "react";
import {Link} from "react-router-dom";


export default function Navbar() {
    return (
        <div>
            <nav className="navbar navbar-expand-lg navbar-light shadow">
                <Link className="btn btn-outline-light ms-2" to="/">Home</Link>
            </nav>
        </div>
    );
}