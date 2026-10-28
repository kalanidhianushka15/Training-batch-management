import { useEffect, useState } from "react";
import axios from "axios";

const API = "http://localhost:8080/api";
const emptyBatch = { batchName: "", courseName: "", startDate: "" };

function App() {
  const [batches, setBatches] = useState([]);
  const [students, setStudents] = useState([]); // all students (each has .batch)
  const [batchForm, setBatchForm] = useState(emptyBatch);
  const [studentForm, setStudentForm] = useState({ name: "", rollNo: "", batchId: "" });
  const [search, setSearch] = useState("");
  const [results, setResults] = useState(null); // search results (null = no search yet)
  const [message, setMessage] = useState({ text: "", type: "" });

  const showMessage = (text, type) => {
    setMessage({ text, type });
    setTimeout(() => setMessage({ text: "", type: "" }), 3000);
  };
  const errorText = (err) =>
    err.response?.data?.message || "Cannot reach backend. Is Spring Boot running?";

  const loadAll = () => {
    axios.get(`${API}/batches`).then((res) => setBatches(res.data)).catch((err) => showMessage(errorText(err), "error"));
    axios.get(`${API}/students`).then((res) => setStudents(res.data)).catch(() => {});
  };

  useEffect(() => {
    loadAll();
  }, []);

  const addBatch = (e) => {
    e.preventDefault();
    if (!batchForm.batchName.trim() || !batchForm.courseName.trim() || !batchForm.startDate) return showMessage("Batch name, course and start date are required", "error");
    axios.post(`${API}/batches`, batchForm)
      .then(() => { showMessage("Batch created", "success"); setBatchForm(emptyBatch); loadAll(); })
      .catch((err) => showMessage(errorText(err), "error"));
  };

  const deleteBatch = (id) => {
    if (window.confirm("Delete this batch and its students?")) {
      axios.delete(`${API}/batches/${id}`)
        .then(() => { showMessage("Batch deleted", "success"); loadAll(); })
        .catch((err) => showMessage(errorText(err), "error"));
    }
  };

  const addStudent = (e) => {
    e.preventDefault();
    if (!studentForm.name.trim() || !studentForm.rollNo.trim()) return showMessage("Name and roll number are required", "error");
    if (!studentForm.batchId) return showMessage("Please select a batch", "error");
    const body = { name: studentForm.name, rollNo: studentForm.rollNo, batch: { id: Number(studentForm.batchId) } };
    axios.post(`${API}/students`, body)
      .then(() => {
        showMessage("Student added to batch", "success");
        setStudentForm({ ...studentForm, name: "", rollNo: "" });
        loadAll();
      })
      .catch((err) => showMessage(errorText(err), "error")); // e.g. duplicate roll no in same batch
  };

  const deleteStudent = (id) => {
    axios.delete(`${API}/students/${id}`)
      .then(() => { showMessage("Student removed", "success"); loadAll(); setResults(null); })
      .catch((err) => showMessage(errorText(err), "error"));
  };

  const doSearch = (e) => {
    e.preventDefault();
    if (!search.trim()) return setResults(null);
    axios.get(`${API}/students`, { params: { rollNo: search } })
      .then((res) => setResults(res.data))
      .catch((err) => showMessage(errorText(err), "error"));
  };

  const studentsOf = (batchId) => students.filter((s) => s.batch.id === batchId);

  return (
    <div className="container">
      <h1>Training Batch Management</h1>

      {message.text && <div className={`msg ${message.type}`}>{message.text}</div>}

      <h2>Create Batch</h2>
      <form onSubmit={addBatch}>
        <input placeholder="Batch name (e.g. FSD-B1)" value={batchForm.batchName} onChange={(e) => setBatchForm({ ...batchForm, batchName: e.target.value })} />
        <input placeholder="Course name" value={batchForm.courseName} onChange={(e) => setBatchForm({ ...batchForm, courseName: e.target.value })} />
        <input type="date" value={batchForm.startDate} onChange={(e) => setBatchForm({ ...batchForm, startDate: e.target.value })} />
        <button type="submit">Create Batch</button>
      </form>

      <h2>Add Student to Batch</h2>
      <form onSubmit={addStudent}>
        <input placeholder="Student name" value={studentForm.name} onChange={(e) => setStudentForm({ ...studentForm, name: e.target.value })} />
        <input placeholder="Roll No" value={studentForm.rollNo} onChange={(e) => setStudentForm({ ...studentForm, rollNo: e.target.value })} />
        <select value={studentForm.batchId} onChange={(e) => setStudentForm({ ...studentForm, batchId: e.target.value })}>
          <option value="">-- Select batch --</option>
          {batches.map((b) => <option key={b.id} value={b.id}>{b.batchName}</option>)}
        </select>
        <button type="submit">Add Student</button>
      </form>

      <h2>Search Student by Roll No</h2>
      <form onSubmit={doSearch}>
        <input placeholder="Roll number" value={search} onChange={(e) => setSearch(e.target.value)} />
        <button type="submit">Search</button>
        <button type="button" className="grey" onClick={() => { setSearch(""); setResults(null); }}>Clear</button>
      </form>
      {results && (
        results.length === 0 ? (
          <div className="msg error">No student found with roll number "{search}"</div>
        ) : (
          <table style={{ marginBottom: 20 }}>
            <thead><tr><th>Roll No</th><th>Name</th><th>Batch</th><th>Course</th></tr></thead>
            <tbody>
              {results.map((s) => (
                <tr key={s.id}><td>{s.rollNo}</td><td>{s.name}</td><td>{s.batch.batchName}</td><td>{s.batch.courseName}</td></tr>
              ))}
            </tbody>
          </table>
        )
      )}

      <h2>Batches</h2>
      {batches.length === 0 && <p>No batches yet</p>}
      {batches.map((b) => {
        const list = studentsOf(b.id);
        return (
          <div key={b.id} style={{ border: "1px solid #ccc", padding: 15, marginBottom: 15 }}>
            <div className="toolbar" style={{ marginBottom: 10 }}>
              <div>
                <b>{b.batchName}</b> · {b.courseName} · starts {b.startDate}
              </div>
              <div>
                <span className="badge blue">{list.length} student(s)</span>{" "}
                <button className="delete small" onClick={() => deleteBatch(b.id)}>Delete Batch</button>
              </div>
            </div>
            <table>
              <thead><tr><th>Roll No</th><th>Name</th><th></th></tr></thead>
              <tbody>
                {list.length === 0 ? (
                  <tr><td colSpan="3">No students in this batch</td></tr>
                ) : (
                  list.map((s) => (
                    <tr key={s.id}>
                      <td>{s.rollNo}</td><td>{s.name}</td>
                      <td><button className="delete small" onClick={() => deleteStudent(s.id)}>Remove</button></td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        );
      })}
    </div>
  );
}

export default App;
