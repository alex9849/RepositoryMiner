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

  _parseProject(project) {
    project.lastUpdate = new Date(project.lastUpdate);
  }

}

export default new ProjectService();
