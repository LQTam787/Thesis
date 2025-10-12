from flask import Flask, request, jsonify
from .nlp_service import process_text_for_nutrition_analysis
from .vision_service import analyze_image_for_food_recognition
from .recommendation_service import generate_nutrition_recommendations

app = Flask(__name__)

@app.route('/api/ai/nlp/analyze', methods=['POST'])
def nlp_analyze():
    data = request.get_json()
    text = data.get('text')
    if not text:
        return jsonify({'error': 'Missing text parameter'}), 400
    
    result = process_text_for_nutrition_analysis(text)
    return jsonify(result)

@app.route('/api/ai/vision/analyze', methods=['POST'])
def vision_analyze():
    # In a real application, you would handle image file uploads
    # For simplicity, we'll assume a base64 encoded image or a URL
    data = request.get_json()
    image_data = data.get('image_data') # base64 encoded image
    if not image_data:
        return jsonify({'error': 'Missing image_data parameter'}), 400
    
    result = analyze_image_for_food_recognition(image_data)
    return jsonify(result)

@app.route('/api/ai/recommendation/generate', methods=['POST'])
def recommendation_generate():
    data = request.get_json()
    user_profile = data.get('user_profile')
    dietary_preferences = data.get('dietary_preferences')
    health_goals = data.get('health_goals')

    if not user_profile:
        return jsonify({'error': 'Missing user_profile parameter'}), 400
    
    recommendations = generate_nutrition_recommendations(user_profile, dietary_preferences, health_goals)
    return jsonify(recommendations)

if __name__ == '__main__':
    app.run(debug=True, port=5000)
