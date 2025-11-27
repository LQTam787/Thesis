// src/pages/admin/AITrainingTriggerPage.jsx
import React, { useState } from 'react';
import adminService from '../../services/adminService';
import { Cpu, Zap, Loader, CheckCircle, AlertTriangle } from 'lucide-react';

function AITrainingTriggerPage() {
    const [trainingStatus, setTrainingStatus] = useState(null); // null, 'loading', 'success', 'error'
    const [jobDetails, setJobDetails] = useState(null); // Thông tin trả về từ Backend

    const handleTriggerRetraining = async () => {
        // Xác nhận trước khi thực hiện hành động tốn tài nguyên
        if (!window.confirm('Bạn có chắc chắn muốn kích hoạt Huấn luyện lại mô hình AI không? Quá trình này có thể tốn thời gian và tài nguyên máy chủ.')) {
            return;
        }

        setTrainingStatus('loading');
        setJobDetails(null);

        try {
            const result = await adminService.triggerAIRetraining();
            setJobDetails(result);
            setTrainingStatus('success');
        } catch (err) {
            setTrainingStatus('error');
            // Giả định một cấu trúc lỗi đơn giản
            setJobDetails({
                message: 'Không thể kích hoạt quá trình huấn luyện lại. Vui lòng kiểm tra log Backend.',
                errorDetail: err.response?.data?.message || err.message
            });
        }
    };

    const getStatusIcon = () => {
        switch (trainingStatus) {
            case 'loading':
                return <Loader className="w-8 h-8 animate-spin text-blue-500" />;
            case 'success':
                return <CheckCircle className="w-8 h-8 text-green-500" />;
            case 'error':
                return <AlertTriangle className="w-8 h-8 text-red-500" />;
            default:
                return <Zap className="w-8 h-8 text-yellow-500" />;
        }
    };

    const getStatusMessage = () => {
        switch (trainingStatus) {
            case 'loading':
                return 'Đang gửi yêu cầu kích hoạt...';
            case 'success':
                return 'Yêu cầu huấn luyện lại AI đã được kích hoạt thành công!';
            case 'error':
                return 'Kích hoạt thất bại.';
            default:
                return 'Chờ lệnh kích hoạt từ Quản trị viên.';
        }
    };

    return (
        <div className="min-h-screen bg-gray-50 p-6 md:p-10">
            <h1 className="text-3xl font-bold text-gray-800 mb-8 flex items-center">
                <Cpu className="w-7 h-7 mr-3 text-indigo-600" />
                Quản lý AI: Kích hoạt Huấn luyện lại
            </h1>

            <div className="bg-white p-8 rounded-xl shadow-2xl max-w-2xl mx-auto border-t-4 border-indigo-500">

                <div className="text-center mb-6">
                    <div className="mx-auto mb-4 w-12 h-12 flex items-center justify-center rounded-full bg-indigo-100">
                        {getStatusIcon()}
                    </div>
                    <h2 className="text-xl font-bold text-gray-800">{getStatusMessage()}</h2>
                    <p className="text-sm text-gray-500 mt-1">Huấn luyện lại sẽ sử dụng dữ liệu mới nhất (Thực phẩm/Món ăn, Nhật ký người dùng) để cải thiện độ chính xác.</p>
                </div>

                {/* Thông tin Chi tiết Job */}
                {jobDetails && (
                    <div className={`p-4 mt-4 border rounded-lg ${trainingStatus === 'success' ? 'bg-green-50 border-green-300' : 'bg-red-50 border-red-300'}`}>
                        <p className="font-semibold text-gray-700 mb-1">Chi tiết Job:</p>
                        {trainingStatus === 'success' ? (
                            <>
                                <p className="text-sm"><strong>Trạng thái:</strong> <span className="text-green-600 font-medium">{jobDetails.status || 'ĐÃ BẮT ĐẦU'}</span></p>
                                <p className="text-sm"><strong>Job ID:</strong> {jobDetails.jobId || 'N/A'}</p>
                                <p className="text-sm"><strong>Thời gian:</strong> {new Date().toLocaleString()}</p>
                                <p className="text-xs mt-2 text-green-700">Lưu ý: Job này chạy bất đồng bộ (Background). Bạn có thể cần kiểm tra trạng thái trên log Backend.</p>
                            </>
                        ) : (
                            <>
                                <p className="text-sm text-red-700"><strong>Lỗi:</strong> {jobDetails.message}</p>
                                {jobDetails.errorDetail && <p className="text-xs text-red-500 mt-1">Chi tiết: {jobDetails.errorDetail}</p>}
                            </>
                        )}
                    </div>
                )}

                {/* Nút Kích hoạt */}
                <button
                    onClick={handleTriggerRetraining}
                    disabled={trainingStatus === 'loading'}
                    className={`w-full py-3 mt-6 text-white font-bold rounded-lg shadow-md transition duration-150 flex items-center justify-center ${
                        trainingStatus === 'loading' ? 'bg-gray-400 cursor-not-allowed' : 'bg-indigo-600 hover:bg-indigo-700'
                    }`}
                >
                    {trainingStatus === 'loading' ? (
                        <>
                            <Loader className="w-5 h-5 mr-3 animate-spin" />
                            Đang Kích hoạt...
                        </>
                    ) : (
                        <>
                            <Cpu className="w-5 h-5 mr-3" />
                            Kích hoạt Huấn luyện lại AI NGAY BÂY GIỜ
                        </>
                    )}
                </button>

            </div>
        </div>
    );
}

export default AITrainingTriggerPage;