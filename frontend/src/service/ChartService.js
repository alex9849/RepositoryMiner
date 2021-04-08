import PackedBubbleService from "src/service/chartServices/PackedBubbleService";
import PieChartService from "src/service/chartServices/PieChartService";

class ChartService {
  parseBackendToOptions(backendData) {
    if(backendData.type === 'packedbubble') {
      return PackedBubbleService.parseBackendToOptions(backendData);
    }
    if(backendData.type === 'pie') {
      return PieChartService.parseBackendToOptions(backendData);
    }
    return null;
  }
}

export default new ChartService();
