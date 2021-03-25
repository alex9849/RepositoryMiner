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
    <q-form>
      <q-input
        type="textarea"
        outlined
        filled
        label="Copy git log here"
      />
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
          color="positive"
          :label="isNewProject? 'Add':'Update'"
          style="width: 150px"
        />
      </div>
    </q-form>
  </q-page>
</template>

<script>
export default {
  name: "EditProject",
  data: () => {
    return {
      gitCommand: "git log --pretty=format:'[%h] %an %ad %s' --date=\"format:%Y-%m-%d %H:%M:%S\" --numstat --summary --reverse >> repolog.log",
      isGitCommandCopied: false
    }
  },
  methods: {
    onCopyGitCommand() {
      navigator.clipboard.writeText(this.gitCommand);
      this.isGitCommandCopied = true;
      setInterval(() => {
        this.isGitCommandCopied = false;
      }, 2000)
    }
  },
  computed: {
    isNewProject() {
      return this.$route.name === 'addProject';
    }
  }
}
</script>

<style scoped>

</style>
