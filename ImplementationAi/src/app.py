"""Main Flask application for the AI services.

This module sets up a Flask web server with three main endpoints for AI-powered nutritional analysis:
- /api/ai/nlp/analyze: Analyzes text to extract food items and nutritional information.
- /api/ai/vision/analyze: Analyzes an image to recognize food items.
- /api/ai/recommendation/generate: Generates personalized nutrition recommendations.

It uses Flasgger for Swagger UI documentation and Pydantic for request validation.
"""
from flask import Flask, jsonify
from flasgger import Swagger
from flask_pydantic import validate

# Luồng hoạt động: Nhập các hàm logic nghiệp vụ từ các dịch vụ AI.
from nlp_service import process_text_for_nutrition_analysis
from vision_service import analyze_image_for_food_recognition
from recommendation_service import generate_nutrition_recommendations
# Luồng hoạt động: Nhập các schema Pydantic để xác thực yêu cầu API.
from schemas import NlpRequest, VisionRequest, RecommendationRequest

app = Flask(__name__)
# Nguyên lý: Khởi tạo Flasgger để tự động tạo tài liệu Swagger/OpenAPI từ docstring của các route.
swagger = Swagger(app)

# Luồng hoạt động: Định nghĩa endpoint cho dịch vụ NLP.
@app.route('/api/ai/nlp/analyze', methods=['POST'])
# Nguyên lý: Decorator @validate() từ Flask-Pydantic tự động xác thực body yêu cầu theo schema NlpRequest.
@validate()
def nlp_analyze(body: NlpRequest):
    """
    Analyze text for nutrition information.
    ---
    tags:
      - NLP
    parameters:
      - in: body
        name: body
        schema: NlpRequest
    responses:
      200:
        description: Analysis result
    """
    # Luồng hoạt động: Gọi hàm xử lý chính và trả về kết quả dưới dạng JSON.
    # Luồng dữ liệu API: Nhận văn bản đầu vào từ body, chuyển đến process_text_for_nutrition_analysis.
    result = process_text_for_nutrition_analysis(body.text)
    return jsonify(result)

# Luồng hoạt động: Định nghĩa endpoint cho dịch vụ Vision.
@app.route('/api/ai/vision/analyze', methods=['POST'])
@validate()
def vision_analyze(body: VisionRequest):
    """
    Analyze an image for food recognition.
    ---
    tags:
      - Vision
    parameters:
      - in: body
        name: body
        schema: VisionRequest
    responses:
      200:
        description: Recognition result
    """
    # Luồng hoạt động: Gọi hàm xử lý hình ảnh và trả về kết quả dưới dạng JSON.
    # Luồng dữ liệu API: Nhận dữ liệu hình ảnh Base64, chuyển đến analyze_image_for_food_recognition.
    result = analyze_image_for_food_recognition(body.image_data)
    return jsonify(result)

# Luồng hoạt động: Định nghĩa endpoint cho dịch vụ Khuyến nghị.
@app.route('/api/ai/recommendation/generate', methods=['POST'])
@validate()
def recommendation_generate(body: RecommendationRequest):
    """
    Generate nutrition recommendations.
    ---
    tags:
      - Recommendation
    parameters:
      - in: body
        name: body
        schema: RecommendationRequest
    responses:
      200:
        description: Recommendation result
    """
    # Luồng hoạt động: Gọi hàm tạo khuyến nghị với các tham số từ body.
    # Luồng dữ liệu API: Nhận hồ sơ người dùng, sở thích, mục tiêu, chuyển đến generate_nutrition_recommendations.
    recommendations = generate_nutrition_recommendations(
        body.user_profile,
        body.dietary_preferences,
        body.nutrition_goal_natural_language
    )
    return jsonify(recommendations)

if __name__ == '__main__':
    # Luồng hoạt động: Khởi chạy máy chủ web Flask ở chế độ gỡ lỗi (debug) trên cổng 5000.
    app.run(debug=True, port=5000)