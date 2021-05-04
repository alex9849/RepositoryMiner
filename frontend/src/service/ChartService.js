import PackedBubbleService from "src/service/chartServices/PackedBubbleService";
import PieChartService from "src/service/chartServices/PieChartService";
import LineChartService from "src/service/chartServices/LineChartService";
import BarChartService from "src/service/chartServices/BarChartService";
import HeatMapChartService from "src/service/chartServices/HeatMapChartService";

class ChartService {
  parseBackendToOptions(backendData) {
    if(backendData.type === 'packedbubble') {
      return PackedBubbleService.parseBackendToOptions(backendData);
    }
    if(backendData.type === 'pie') {
      return PieChartService.parseBackendToOptions(backendData);
    }
    if(backendData.type === 'line') {
      return LineChartService.parseBackendToOptions(backendData);
    }
    if(backendData.type === 'bar') {
      return BarChartService.parseBackendToOptions(backendData);
    }
    if(backendData.type === 'heatmap') {
      return HeatMapChartService.parseBackendToOptions(backendData);
    }
    return null;
  }
}

export default new ChartService();
