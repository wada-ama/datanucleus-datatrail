FROM adoptopenjdk/maven-openjdk8 as maven


# Suppress the configuration prompts
ARG DEBIAN_FRONTEND=noninteractive

# Add a non-root user with sudo access
ARG USERNAME=maven_builder
ARG USER_UID=1000
ARG USER_GID=$USER_UID
ARG USER_HOME=/workspace

RUN \
  # Update system and install sudo to be able to chown mounted volumes
  apt-get update && apt-get -qy --no-install-recommends install gcc-multilib sudo; \
  # Create a non-root user
  groupadd -g ${USER_GID} ${USERNAME}; \
  useradd -d "${USER_HOME}" -g ${USER_GID} -m -u ${USER_UID} ${USERNAME}; \
  # Add sudo support for the non-root user
  echo "${USERNAME} ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers.d/${USERNAME}; \
  chmod 0440 /etc/sudoers.d/${USERNAME}

RUN \
  # prepare m2 mount
  mkdir /m2 && \
  chown ${USERNAME}:${USERNAME} /m2 && \
  chmod 777 /m2

# Nexus credentials
ARG NEXUS_USERNAME=nexus_usr
ARG NEXUS_PASSWORD=nexus_pwd

# Variables used in the Maven settings
ENV NEXUS_USERNAME ${NEXUS_USERNAME}
ENV NEXUS_PASSWORD ${NEXUS_PASSWORD}
ENV NEXUS_URL https://nexus.wada-ama.org
ENV MAVEN_OPTS="-Dmaven.repo.local=/m2"


FROM maven as builder

USER ${USERNAME}

WORKDIR /workspace
CMD ["mvn", "install"]