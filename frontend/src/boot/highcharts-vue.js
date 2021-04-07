import HighchartsVue from 'highcharts-vue';
import Highcharts from 'highcharts';

const defaultOptions = {
  chart: {
    plotBackgroundColor: null,
    plotBorderWidth: null,
    plotShadow: false,
    type: 'pie'
  },
  title: {
    text: null
  },
  plotOptions: {
    pie: {
      allowPointSelect: true,
      cursor: 'pointer',
      dataLabels: {
        enabled: true,
        format: '<b>{point.name}</b>: {point.percentage:.1f} %'
      }
    },
    packedBubble: {
      layoutAlgorithm: {
        enableSimulation: false
      }
    }
  }
};

export default ({ Vue }) => {
  Vue.use(HighchartsVue);
  Highcharts.setOptions(defaultOptions);
};
