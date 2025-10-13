from flask import Flask, jsonify
from flasgger import Swagger
from flask_pydantic import validate

from .nlp_service import process_text_for_nutrition_analysis
from .vision_service import analyze_image_for_food_recognition
from .recommendation_service import generate_nutrition_recommendations
from .schemas import NlpRequest, VisionRequest, RecommendationRequest

app = Flask(__name__)
swagger = Swagger(app)

@app.route('/api/ai/nlp/analyze', methods=['POST'])
@validate()
def nlp_analyze(body: NlpRequest):
    """
    Analyze text for nutrition information.
    """
    result = process_text_for_nutrition_analysis(body.text)
    return jsonify(result)

@app.route('/api/ai/vision/analyze', methods=['POST'])
@validate()
def vision_analyze(body: VisionRequest):
    """
    Analyze an image for food recognition.
    """
    result = analyze_image_for_food_recognition(body.image_data)
    return jsonify(result)

@app.route('/api/ai/recommendation/generate', methods=['POST'])
@validate()
def recommendation_generate(body: RecommendationRequest):
    """
    Generate nutrition recommendations.
    """
    recommendations = generate_nutrition_recommendations(
        body.user_profile,
        body.dietary_preferences,
        body.health_goals
    )
    return jsonify(recommendations)

if __name__ == '__main__':
    app.run(debug=True, port=5000)
