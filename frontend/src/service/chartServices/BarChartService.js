class BarChartService {

  exampleBackendData = {
    name: 'Bar chart',
    type: 'bar',
    description: 'A bar chart',
    xAxisTitle: 'Number',
    categories: ['c1', 'c2', 'c3'],
    series: [{
      name: 'Citizens1',
      data: [30, 20, 40]
    }, {
      name: 'Citizens2',
      data: [35, 15, 45]
    }, {
      name: 'Citizens3',
      data: [8, 20, 15]
    }]
  }

  parseBackendToOptions(backendData) {
    let graphConfig = {
      chart: {
        type: 'bar'
      },
      yAxis: {
        title: {
          text: backendData.yAxisTitle
        }
      },
      xAxis: {
        categories: backendData.categories
      }
    }

    graphConfig.series = backendData.series;

    return {
      graphConfig: graphConfig,
      name: backendData.name,
      description: backendData.description
    }
  }

}

export default new BarChartService();
