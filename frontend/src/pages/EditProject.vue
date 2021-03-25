<template>
  <q-page
    padding
    class="q-gutter-lg"
  >
    <h4
      class="row justify-center"
    >
      {{ isNewProject ? 'Add' : 'Edit' }} Project
    </h4>
    <div
      class="q-gutter-xs"
    >
      <div
        class="row justify-center"
      >
        Copy & execute this command and paste the resulting repolog.log file in the textbox below
      </div>
      <div
        class="row justify-center"
      >
        <q-input
          standout
          dense
          readonly
          :value="gitCommand"
        >
          <template slot="append">
            <q-btn
              rounded
              :icon="isGitCommandCopied? 'done':'file_copy'"
              :label="isGitCommandCopied? '':'Copy'"
              color="primary text-white"
              @click="onCopyGitCommand"
            />
          </template>
        </q-input>
      </div>
    </div>
    <div
      class="row justify-center"
    >
      <q-form
        @submit.prevent="onSubmit"
        style="width: 600px"
      >
        <q-input
          label="Name"
          filled
          v-model="projectName"
          class="q-pa-md"
        />
        <q-file
          class="q-pa-md"
          filled
          v-model="uploadFile"
          label="Upload git-log here"
        >
          <template v-slot:prepend>
            <q-icon name="cloud_upload"/>
          </template>
        </q-file>

        <div
          class="row justify-center q-pa-md q-gutter-lg"
        >
          <q-btn
            rounded
            color="negative"
            label="Abort"
            style="width: 150px"
            @click="$router.push({name: 'projects'})"
          />
          <q-btn
            rounded
            :loading="submitting"
            :disable="$v.$invalid"
            color="positive"
            :label="isNewProject? 'Add':'Update'"
            style="width: 150px"
            type="submit"
          />
        </div>
      </q-form>
    </div>
  </q-page>
</template>

<script>

import ProjectService from "../service/ProjectService";
import {required} from "vuelidate/lib/validators";

export default {
  name: "EditProject",
  data: () => {
    return {
      gitCommand: "git log --pretty=format:'[%h] %an %ad %s' --date=\"format:%Y-%m-%d %H:%M:%S\" --numstat --summary --reverse >> repolog.log",
      isGitCommandCopied: false,
      uploadFile: null,
      projectName: '',
      submitting: false
    }
  },
  methods: {
    onCopyGitCommand() {
      navigator.clipboard.writeText(this.gitCommand);
      this.isGitCommandCopied = true;
      setInterval(() => {
        this.isGitCommandCopied = false;
      }, 2000)
    },
    onSubmit() {
      this.submitting = true;
      ProjectService.createProject(this.projectName, this.uploadFile)
        .then(() => {
          this.$q.notify({
            type: 'positive',
            message: 'Project created successfully'
          });
          this.$router.push({name: 'projects'})
        }, error => {
          this.$q.notify({
            type: 'negative',
            message: 'Couldn\'t create project: ' + error.response.data.message
          });
        })
        .finally(() => {
          this.submitting = false;
        })
    }
  },
  validations() {
    return {
      uploadFile: {
        required
      },
      projectName: {
        required
      }
    }
  }
}
</script>

<style scoped>

</style>
