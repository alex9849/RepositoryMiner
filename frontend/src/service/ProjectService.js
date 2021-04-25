import axios from 'axios';

const API_PATH = 'api/project/';

class ProjectService {

  createProject(projectName, gitLogFile) {
    let uploadData = new FormData();
    uploadData.append("name", projectName);
    uploadData.append("logfile", gitLogFile);

    let config = {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }

    return axios.post(API_PATH, uploadData, config)
      .then(response => response.data);
  }

  saveAuthorsAndLogAuthorGroups(projectId, authorList){
    return axios.put(API_PATH + projectId + "/author", authorList );
  }

  getProjects() {
    return axios.get(API_PATH)
      .then(response => {
        for(let project of response.data) {
          this._parseProject(project);
        }
        return response.data;
      });
  }

  deleteProject(id) {
    return axios.delete(API_PATH + id);
  }

  getProjectFileTree(projectId) {
    return axios.get(API_PATH + projectId + "/structure")
      .then(response => response.data);
  }

  getRequestableProjectCharts(projectId, context) {
    return axios.get(API_PATH + projectId + "/chart", { params: { context } })
      .then(response => response.data);
  }

  getChart(projectId, chartName, requestMeta) {
    let config = {
      params: requestMeta
    }
    return axios.get(API_PATH + projectId + "/chart/" + chartName, config)
      .then(response => response.data);
  }

  getLogAuthors(projectId) {
    return axios.get(API_PATH + projectId + "/logauthor")
      .then(response => response.data);
  }


  getAuthors(projectId) {
    return axios.get(API_PATH + projectId + "/author")
      .then(response => response.data);
  }

  _parseProject(project) {
    project.lastUpdate = new Date(project.lastUpdate);
  }

}

export default new ProjectService();
