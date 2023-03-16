import React, {useEffect, useState} from "react";
import axios from "axios";

export default function Home() {
    const [file, setFile] = useState(null)

    const onSubmit = async (event) => {
        let formData = new FormData()
        formData.append("file", file)
        await axios.post("http://localhost:8080/".concat(event.target.name), formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            },
            responseType: "blob"
        })
            .then(resp => {
                let headerLine = resp.headers['content-disposition'];
                let startFileNameIndex = headerLine.indexOf('"') + 1;
                let endFileNameIndex = headerLine.lastIndexOf('"');
                let filename = headerLine.substring(startFileNameIndex, endFileNameIndex);

                const url = window.URL.createObjectURL(new Blob([resp.data]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', filename);
                document.body.appendChild(link);
                link.click();
                
                document.body.removeChild(link);
                URL.revokeObjectURL(url);
            })
            .catch(err => {
                alert(err.message)
            })
    }
    return (
        <div>
            <div className="shadow px-5 py-5">
                <input className="form-control-lg container-fluid"
                       type="file"
                       onChange={event => setFile(event.target.files[0])}/>
            </div>
            <div className="btn-group-lg mt-5 centeredDiv">
                <button onClick={onSubmit} className="mx-lg-5 btn btn-outline-dark" name="compress">COMPRESS</button>
                <button onClick={onSubmit} className="mx-lg-5 btn btn-outline-secondary" name="decompress">DECOMPRESS</button>
            </div>
        </div>
    );
}