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

from nlp_service import process_text_for_nutrition_analysis
from vision_service import analyze_image_for_food_recognition
from recommendation_service import generate_nutrition_recommendations
from schemas import NlpRequest, VisionRequest, RecommendationRequest

app = Flask(__name__)
swagger = Swagger(app)

@app.route('/api/ai/nlp/analyze', methods=['POST'])
@validate()
def nlp_analyze(body: NlpRequest):
    """Analyzes text to extract nutritional information.

    This endpoint receives a JSON object with a 'text' field and uses the `nlp_service` 
    to process it. The result is a JSON object containing the original text, detected 
    food items, and a nutritional summary.

    Args:
        body (NlpRequest): A Pydantic model representing the request body, which 
                         contains the text to be analyzed.

    Returns:
        A JSON response with the analysis results.
    """
    result = process_text_for_nutrition_analysis(body.text)
    return jsonify(result)

@app.route('/api/ai/vision/analyze', methods=['POST'])
@validate()
def vision_analyze(body: VisionRequest):
    """Analyzes an image to recognize food items.

    This endpoint receives a JSON object with 'image_data' (a base64-encoded string) 
    and uses the `vision_service` to identify food items in the image. The result is a 
    JSON object containing the detected food items and their confidence scores.

    Args:
        body (VisionRequest): A Pydantic model representing the request body, which 
                            contains the base64-encoded image data.

    Returns:
        A JSON response with the recognition results.
    """
    result = analyze_image_for_food_recognition(body.image_data)
    return jsonify(result)

@app.route('/api/ai/recommendation/generate', methods=['POST'])
@validate()
def recommendation_generate(body: RecommendationRequest):
    """Generates personalized nutrition recommendations.

    This endpoint receives a JSON object with user profile information, dietary 
    preferences, and health goals. It uses the `recommendation_service` to generate 
    a personalized meal plan and nutritional advice.

    Args:
        body (RecommendationRequest): A Pydantic model representing the request body, 
                                    which contains user data for recommendations.

    Returns:
        A JSON response with the generated recommendations.
    """
    recommendations = generate_nutrition_recommendations(
        body.user_profile,
        body.dietary_preferences,
        body.health_goals
    )
    return jsonify(recommendations)

if __name__ == '__main__':
    app.run(debug=True, port=5000)
