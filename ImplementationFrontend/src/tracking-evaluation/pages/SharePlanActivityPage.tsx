import React, { useState, useEffect } from 'react';
import '../../index.css'; // Adjust path as necessary for global styles
import '../../App.css'; // Adjust path as necessary for global styles

const SharePlanActivityPage: React.FC = () => {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [nutritionPlan, setNutritionPlan] = useState('');
    const [publicShare, setPublicShare] = useState(false);
    const [message, setMessage] = useState<{ type: string, text: string } | null>(null);
    const [nutritionPlans, setNutritionPlans] = useState<Array<{ id: string, name: string }>>([]);

    // Simulate fetching nutrition plans
    useEffect(() => {
        // In a real application, you would fetch this from an API
        const fetchNutritionPlans = async () => {
            await new Promise(resolve => setTimeout(resolve, 500)); // Simulate network delay
            setNutritionPlans([
                { id: 'plan1', name: 'Kế hoạch Giảm cân tháng 10' },
                { id: 'plan2', name: 'Kế hoạch Tăng cơ bắp' },
                { id: 'plan3', name: 'Kế hoạch Duy trì cân nặng' },
            ]);
        };
        fetchNutritionPlans();
    }, []);

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        setMessage(null);

        // Basic validation
        if (!title.trim() || !content.trim()) {
            setMessage({ type: 'error', text: 'Vui lòng nhập đầy đủ tiêu đề và nội dung.' });
            return;
        }

        // Simulate API call to share content
        console.log('Đang chia sẻ:', { title, content, nutritionPlan, publicShare });

        try {
            // Replace with actual API call (e.g., axios.post('/api/share', { title, content, nutritionPlan, publicShare }))
            await new Promise(resolve => setTimeout(resolve, 1000)); // Simulate network request

            setMessage({ type: 'success', text: 'Nội dung của bạn đã được chia sẻ thành công!' });
            // Optionally clear form
            setTitle('');
            setContent('');
            setNutritionPlan('');
            setPublicShare(false);
        } catch (error) {
            console.error('Lỗi khi chia sẻ nội dung:', error);
            setMessage({ type: 'error', text: 'Có lỗi xảy ra khi chia sẻ nội dung. Vui lòng thử lại.' });
        }
    };

    return (
        <div className="container">
            <h1>Chia Sẻ Kế Hoạch & Hoạt Động</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="title">Tiêu đề:</label>
                    <input
                        type="text"
                        id="title"
                        name="title"
                        placeholder="Nhập tiêu đề nội dung chia sẻ"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="content">Nội dung:</label>
                    <textarea
                        id="content"
                        name="content"
                        placeholder="Viết nội dung chia sẻ của bạn ở đây"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        required
                    ></textarea>
                </div>
                <div className="form-group">
                    <label htmlFor="nutritionPlan">Liên kết với Kế hoạch Dinh dưỡng (Tùy chọn):</label>
                    <select
                        id="nutritionPlan"
                        name="nutritionPlan"
                        value={nutritionPlan}
                        onChange={(e) => setNutritionPlan(e.target.value)}
                    >
                        <option value="">-- Chọn kế hoạch dinh dưỡng --</option>
                        {nutritionPlans.map(plan => (
                            <option key={plan.id} value={plan.id}>{plan.name}</option>
                        ))}
                    </select>
                </div>
                <div className="checkbox-group">
                    <input
                        type="checkbox"
                        id="publicShare"
                        name="publicShare"
                        checked={publicShare}
                        onChange={(e) => setPublicShare(e.target.checked)}
                    />
                    <label htmlFor="publicShare">Chia sẻ công khai</label>
                </div>
                <div className="actions">
                    <button type="submit" className="btn-share">Chia Sẻ</button>
                    <button type="button" className="btn-cancel" onClick={() => window.history.back()}>Hủy</button>
                </div>
            </form>
            {message && (
                <div className={`message ${message.type}`}>
                    {message.text}
                </div>
            )}
        </div>
    );
};

export default SharePlanActivityPage;
