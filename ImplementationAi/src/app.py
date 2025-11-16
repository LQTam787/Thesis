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

# Logic: Import business logic functions from AI services.
from .nlp_service import process_text_for_nutrition_analysis
from .vision_service import analyze_image_for_food_recognition
from .recommendation_service import generate_nutrition_recommendations
# Logic: Import Pydantic schemas for API request validation.
from .schemas import NlpRequest, VisionRequest, RecommendationRequest

app = Flask(__name__)
# Logic: Initialize Flasgger to automatically generate Swagger/OpenAPI documentation from route docstrings.
swagger = Swagger(app)

# Logic: Define endpoint for the NLP service.
@app.route('/api/ai/nlp/analyze', methods=['POST'])
# Logic: The @validate() decorator from Flask-Pydantic automatically validates the request body against the NlpRequest schema.
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
    # Flow: Call the main processing function and return the result as JSON.
    # Data Flow API: Receives text input from the body, passes it to process_text_for_nutrition_analysis.
    result = process_text_for_nutrition_analysis(body.text)
    return jsonify(result)

# Logic: Define endpoint for the Vision service.
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
    # Flow: Call the image processing function and return the result as JSON.
    # Data Flow API: Receives Base64 image data, passes it to analyze_image_for_food_recognition.
    result = analyze_image_for_food_recognition(body.image_data)
    return jsonify(result)

# Logic: Define endpoint for the Recommendation service.
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
    # Flow: Call the recommendation generation function with parameters from the body.
    # Data Flow API: Receives user profile, dietary preferences, and natural language nutrition goal, passes them to generate_nutrition_recommendations.
    recommendations = generate_nutrition_recommendations(
        body.user_profile,
        body.dietary_preferences,
        body.nutrition_goal_natural_language
    )
    return jsonify(recommendations)

if __name__ == '__main__':
    # Flow: Start the Flask web server in debug mode on port 5000.
    app.run(debug=True, port=5000)