function getPointCategoryName(point, dimension) {
  let series = point.series,
    isY = dimension === 'y',
    axis = series[isY ? 'yAxis' : 'xAxis'];
  return axis.categories[point[isY ? 'y' : 'x']];
}

class HeatMapChartService {

  exampleBackendData = {
    name: 'Heat Map Chart',
    type: 'heatmap',
    description: 'A heatmap chart',
    xAxisTitle: 'x-Axis title',
    yAxisTitle: 'y-Axis title',
    xAxisCategories: ['Test1', 'Test2', 'Test3', 'Test4'],
    yAxisCategories: ['Test1', 'Test2', 'Test3', 'Test4'],
    series: [{
      name: 'Data',
      data: [[0, 0, 1], [0, 1, 0], [0, 2, 0], [0, 3, 0], [0, 4, 0], [1, 0, 0], [1, 1, 0], [1, 2, 0], [1, 3, 0], [1, 4, 0], [2, 0, 0], [2, 1, 0], [2, 2, 0], [2, 3, 0], [2, 4, 0], [3, 0, 0], [3, 1, 0], [3, 2, 0], [3, 3, 0], [3, 4, 0], [4, 0, 0], [4, 1, 0], [4, 2, 0], [4, 3, 0], [4, 4, 0], [5, 0, 0], [5, 1, 0], [5, 2, 0], [5, 3, 0], [5, 4, 0], [6, 0, 0], [6, 1, 0], [6, 2, 0], [6, 3, 0], [6, 4, 0], [7, 0, 0], [7, 1, 0], [7, 2, 0], [7, 3, 0], [7, 4, 0], [8, 0, 0], [8, 1, 0], [8, 2, 0], [8, 3, 0], [8, 4, 0], [9, 0, 0], [9, 1, 0], [9, 2, 0], [9, 3, 0], [9, 4, 0]]
    }]
  }

  parseBackendToOptions(backendData) {
    let graphConfig = {
      chart: {
        type: 'heatmap'
      },
      yAxis: {
        title: {
          text: backendData.yAxisTitle
        },
        categories: backendData.yAxisCategories
      },
      xAxis: {
        title: {
          text: backendData.xAxisTitle
        },
        categories: backendData.xAxisCategories
      },
      tooltip: {
        formatter: function () {
          return '<b>' + getPointCategoryName(this.point, 'x') + '</b><br>' +
            '<b>' + getPointCategoryName(this.point, 'y') + '</b><br>' +
            this.point.value + ' ' + this.point.series.name;
        }
      },
      colorAxis: {
        stops: [
          [0, '#3060cf'],
          [0.5, '#fffbbc'],
          [0.9, '#c4463a'],
          [1, '#c4463a']
        ],
        startOnTick: false,
        endOnTick: false
      },
    }

    graphConfig.series = backendData.series;

    return {
      graphConfig: graphConfig,
      name: backendData.name,
      description: backendData.description
    }
  }

}

export default new HeatMapChartService();
