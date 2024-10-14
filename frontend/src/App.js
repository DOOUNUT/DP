import React from 'react';
import './App.css';
import Chat from './Chat'; // Chat 컴포넌트를 가져옵니다.
import Sidebar from './Sidebar'; // Sidebar 컴포넌트를 가져옵니다.

function App() {
    return (
        <div className="app">
            <Sidebar />
            <Chat /> {/* Chat 컴포넌트를 여기서 사용 */}
        </div>
    );
}

export default App;
