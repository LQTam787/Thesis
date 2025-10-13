"""Fixtures for testing."""
import base64
import io

import pytest
from PIL import Image


@pytest.fixture(scope="session")
def sample_image_base64():
    """Creates a simple black image and returns it as a base64 string."""
    # Create a simple 10x10 black image
    img = Image.new('RGB', (10, 10), color='black')

    # Save it to a bytes buffer
    buf = io.BytesIO()
    img.save(buf, format='JPEG')
    byte_im = buf.getvalue()

    # Encode to base64
    return base64.b64encode(byte_im).decode('utf-8')
