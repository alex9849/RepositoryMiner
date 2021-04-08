import PackedBubbleService from "src/service/chartServices/PackedBubbleService";

class ChartService {
  parseBackendToOptions(backendData) {
    if(backendData.type === 'packedbubble') {
      return PackedBubbleService.parseBackendToOptions(backendData);
    }
    return null;
  }
}

export default new ChartService();
