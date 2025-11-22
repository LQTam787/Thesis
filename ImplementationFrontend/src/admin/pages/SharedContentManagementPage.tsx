import React, { useEffect, useState } from 'react';
import { SharedContent } from '../types/SharedContent';
import { sharedContentService } from '../services/sharedContentService';
import { Link } from 'react-router-dom';

const SharedContentManagementPage: React.FC = () => {
  const [contents, setContents] = useState<SharedContent[]>([]);
  const [filteredContents, setFilteredContents] = useState<SharedContent[]>([]);
  const [statusFilter, setStatusFilter] = useState<'all' | 'pending' | 'approved' | 'rejected'>('all');
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchContents = async () => {
      try {
        setLoading(true);
        const data = await sharedContentService.getAllSharedContent();
        setContents(data);
        setFilteredContents(data);
      } catch (err) {
        setError('Không thể tải nội dung chia sẻ.');
      } finally {
        setLoading(false);
      }
    };
    fetchContents();
  }, []);

  useEffect(() => {
    filterContent();
  }, [statusFilter, searchTerm, contents]);

  const filterContent = () => {
    let updatedContents = contents;

    if (statusFilter !== 'all') {
      updatedContents = updatedContents.filter(content => content.trangThaiChiaSe === statusFilter);
    }

    if (searchTerm) {
      updatedContents = updatedContents.filter(content =>
        content.tieuDe.toLowerCase().includes(searchTerm.toLowerCase()) ||
        content.noiDung.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
    setFilteredContents(updatedContents);
  };

  const handleStatusChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setStatusFilter(e.target.value as 'all' | 'pending' | 'approved' | 'rejected');
  };

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(e.target.value);
  };

  const viewContent = (contentId: string) => {
    alert(`Chức năng xem nội dung chia sẻ ID: ${contentId}`);
    // Logic để hiển thị chi tiết nội dung hoặc điều hướng đến trang xem chi tiết
  };

  const approveContent = async (contentId: string) => {
    if (window.confirm('Bạn có chắc chắn muốn duyệt nội dung này không?')) {
      try {
        const success = await sharedContentService.updateSharedContentStatus(contentId, 'approved');
        if (success) {
          setContents(prevContents =>
            prevContents.map(content =>
              content.id === contentId ? { ...content, trangThaiChiaSe: 'approved' } : content
            )
          );
          alert(`Duyệt nội dung ID: ${contentId} thành công.`);
        } else {
          alert(`Không thể duyệt nội dung ID: ${contentId}.`);
        }
      } catch (error) {
        alert(`Lỗi khi duyệt nội dung: ${error}`);
      }
    }
  };

  const rejectContent = async (contentId: string) => {
    if (window.confirm('Bạn có chắc chắn muốn từ chối nội dung này không?')) {
      try {
        const success = await sharedContentService.updateSharedContentStatus(contentId, 'rejected');
        if (success) {
          setContents(prevContents =>
            prevContents.map(content =>
              content.id === contentId ? { ...content, trangThaiChiaSe: 'rejected' } : content
            )
          );
          alert(`Từ chối nội dung ID: ${contentId} thành công.`);
        } else {
          alert(`Không thể từ chối nội dung ID: ${contentId}.`);
        }
      } catch (error) {
        alert(`Lỗi khi từ chối nội dung: ${error}`);
      }
    }
  };

  const deleteContent = async (contentId: string) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa nội dung này không?')) {
      try {
        const success = await sharedContentService.deleteSharedContent(contentId);
        if (success) {
          setContents(prevContents => prevContents.filter(content => content.id !== contentId));
          alert(`Xóa nội dung ID: ${contentId} thành công.`);
        } else {
          alert(`Không thể xóa nội dung ID: ${contentId}.`);
        }
      } catch (error) {
        alert(`Lỗi khi xóa nội dung: ${error}`);
      }
    }
  };

  if (loading) return <div className="container mx-auto p-4">Đang tải nội dung...</div>;
  if (error) return <div className="container mx-auto p-4 text-red-500">Lỗi: {error}</div>;

  return (
    <div className="container mx-auto p-4">
      <header className="bg-green-600 text-white p-4 rounded-t-lg text-center mb-4">
        <h1 className="text-3xl font-bold">Bảng điều khiển quản trị viên</h1>
      </header>
      <nav className="flex justify-center bg-gray-800 p-2 rounded-md mb-4">
        <Link to="/admin/dashboard" className="text-white px-4 py-2 hover:bg-gray-700 rounded-md transition-colors duration-300">Trang chủ</Link>
        <Link to="/admin/users" className="text-white px-4 py-2 hover:bg-gray-700 rounded-md transition-colors duration-300">Quản lý người dùng</Link>
        <Link to="/admin/shared-content" className="text-white px-4 py-2 hover:bg-gray-700 rounded-md transition-colors duration-300">Quản lý nội dung chia sẻ</Link>
        <Link to="#" className="text-white px-4 py-2 hover:bg-gray-700 rounded-md transition-colors duration-300">Cài đặt</Link>
        <Link to="/login" className="text-white px-4 py-2 hover:bg-gray-700 rounded-md transition-colors duration-300">Đăng xuất</Link>
      </nav>
      <main>
        <section className="content-management-section bg-white shadow-md rounded-lg p-6">
          <h2 className="text-2xl font-semibold mb-4">Quản lý nội dung chia sẻ</h2>
          <div className="flex justify-between items-center mb-4 space-x-4">
            <div>
              <label htmlFor="status-filter" className="mr-2 font-medium">Trạng thái:</label>
              <select id="status-filter" value={statusFilter} onChange={handleStatusChange} className="border border-gray-300 rounded-md p-2">
                <option value="all">Tất cả</option>
                <option value="pending">Chờ duyệt</option>
                <option value="approved">Đã duyệt</option>
                <option value="rejected">Đã từ chối</option>
              </select>
            </div>
            <div>
              <label htmlFor="search-input" className="mr-2 font-medium">Tìm kiếm:</label>
              <input
                type="text"
                id="search-input"
                placeholder="Tìm kiếm theo tiêu đề..."
                value={searchTerm}
                onChange={handleSearchChange}
                className="border border-gray-300 rounded-md p-2 w-64"
              />
            </div>
          </div>
          <div className="overflow-x-auto">
            <table className="min-w-full bg-white">
              <thead>
                <tr>
                  <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">ID</th>
                  <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Tiêu đề</th>
                  <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Nội dung</th>
                  <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Trạng thái</th>
                  <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Lượt xem</th>
                  <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Lượt thích</th>
                  <th className="py-3 px-4 border-b-2 border-gray-200 bg-gray-100 text-left text-sm font-semibold text-gray-600 uppercase tracking-wider">Hành động</th>
                </tr>
              </thead>
              <tbody>
                {filteredContents.length > 0 ? (
                  filteredContents.map(content => (
                    <tr key={content.id} className="hover:bg-gray-50">
                      <td className="py-3 px-4 border-b border-gray-200 text-sm">{content.id}</td>
                      <td className="py-3 px-4 border-b border-gray-200 text-sm">{content.tieuDe}</td>
                      <td className="py-3 px-4 border-b border-gray-200 text-sm">{content.noiDung.substring(0, 50)}...</td>
                      <td className="py-3 px-4 border-b border-gray-200 text-sm">{content.trangThaiChiaSe}</td>
                      <td className="py-3 px-4 border-b border-gray-200 text-sm">{content.luotXem}</td>
                      <td className="py-3 px-4 border-b border-gray-200 text-sm">{content.luotThich}</td>
                      <td className="py-3 px-4 border-b border-gray-200 text-sm flex space-x-2">
                        <button onClick={() => viewContent(content.id)} className="text-blue-600 hover:text-blue-800">Xem</button>
                        {content.trangThaiChiaSe === 'pending' && (
                          <>
                            <button className="text-green-600 hover:text-green-800" onClick={() => approveContent(content.id)}>Duyệt</button>
                            <button className="text-red-600 hover:text-red-800" onClick={() => rejectContent(content.id)}>Từ chối</button>
                          </>
                        )}
                        {content.trangThaiChiaSe === 'rejected' && (
                          <button className="text-green-600 hover:text-green-800" onClick={() => approveContent(content.id)}>Duyệt</button>
                        )}
                        <button className="text-red-600 hover:text-red-800" onClick={() => deleteContent(content.id)}>Xóa</button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={7} className="py-3 px-4 border-b border-gray-200 text-sm text-center">Không có nội dung nào phù hợp.</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </section>
      </main>
      <footer className="text-center mt-8 p-4 text-gray-600 border-t border-gray-200">
        <p>&copy; 2025 Hệ thống Quản lý Dinh dưỡng. Tất cả quyền được bảo lưu.</p>
      </footer>
    </div>
  );
};

export default SharedContentManagementPage;
