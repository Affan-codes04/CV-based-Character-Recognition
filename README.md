# 🎭 CharacterVision – CV-Based Character Recognition App

An end-to-end Computer Vision application that recognizes fictional characters from images using a deep learning model deployed on Android.

This project covers the complete ML pipeline — from dataset preprocessing and model training to TensorFlow Lite conversion and on-device inference.

---

## 🚀 Overview

CharacterVision is a CNN-based image classification system built using transfer learning (VGG16).  
The trained model is optimized and converted to TensorFlow Lite, enabling real-time character prediction directly on Android devices.

The application performs inference fully on-device without requiring an internet connection.

---

## 🧠 Model Development

### 📂 Dataset & Preprocessing
- Collected a labeled fictional character image dataset
- Resized images to match VGG16 input dimensions
- Applied normalization
- Implemented data augmentation:
  - Rotation
  - Zoom
  - Horizontal flipping
  - Shearing

---

### 🏗 Model Architecture

- Base Model: **VGG16 (Pre-trained on ImageNet)**
- Removed top classification layers
- Added:
  - Global Average Pooling
  - Dense layers
  - Softmax output layer

---

### 🎯 Training Strategy

- Transfer Learning with frozen base layers
- Fine-tuning selected convolutional layers
- Early Stopping to prevent overfitting
- Validation monitoring for performance tracking

---

## 📊 Performance Optimization

- Used data augmentation to improve generalization
- Applied fine-tuning on higher VGG16 layers
- Implemented early stopping based on validation loss
- Reduced model size for mobile compatibility

---

## 📱 Android Integration

- Converted trained Keras model to **TensorFlow Lite (.tflite)**
- Integrated TFLite model into Android app
- Implemented on-device inference
- Enabled real-time image prediction using camera/gallery input

---

## 🛠 Tech Stack

- **Python**
- **TensorFlow / Keras**
- **TensorFlow Lite**
- **Android (Java)**
- **CNN Architecture (VGG16 Transfer Learning)**

---

## 🔄 Workflow

1. Dataset Collection
2. Image Preprocessing & Augmentation
3. Model Training (Transfer Learning)
4. Fine-Tuning & Optimization
5. Convert to TensorFlow Lite
6. Integrate into Android Application
7. Perform On-Device Inference

---

## 📌 Features

- Real-time character prediction
- Offline inference (No internet required)
- Lightweight optimized TFLite model
- Clean Android UI integration

---

## 🚀 Future Improvements

- Expand dataset with more character classes
- Implement confidence threshold filtering
- Add top-3 predictions display
- Improve inference speed with model quantization
- Deploy cloud-based model comparison version

---

## 👨‍💻 Author

**Affan Rahmathullah**  
Software Engineering Student  
VIT Vellore  

---

## 📄 License

This project is developed for educational and research purposes.
