def call(Map configMap) {
    echo "======================================"
    echo "nodejsEKSPipeline shared library loaded"
    echo "======================================"

    echo "Project   : ${configMap.get('project')}"
    echo "Component : ${configMap.get('component')}"
}