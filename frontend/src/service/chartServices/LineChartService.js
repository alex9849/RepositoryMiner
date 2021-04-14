class LineChartService {

  exampleBackendData = {
    name: 'Line chart',
    type: 'line',
    description: 'A line chart',
    xAxisTitle: 'Number',
    yAxisTitle: 'other number',
    series: [{
      name: 'Cities',
      data: [1, 2, 3, 4, 5, 6]
    }, {
      name: 'Citizens',
      data: [10, 20, 30, 40, 50, 60]
    }]
  }

  parseBackendToOptions(backendData) {
    let graphConfig = {
      chart: {
        type: 'line',
        zoomType: 'x'
      },
      yAxis: {
        title: {
          text: backendData.yAxisTitle
        }
      },
      xAxis: {
        title: {
          text: backendData.xAxisTitle
        },
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

export default new LineChartService();
