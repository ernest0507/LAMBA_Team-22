from functools import lru_cache
from os import getenv

from dotenv import load_dotenv


load_dotenv()


class Settings:
    app_name: str = getenv("APP_NAME", "Lamba Car Care API")
    api_prefix: str = getenv("API_PREFIX", "/api/v1")
    debug: bool = getenv("DEBUG", "false").lower() == "true"

    postgres_db: str = getenv("POSTGRES_DB", "lamba_db")
    postgres_user: str = getenv("POSTGRES_USER", "lamba_user")
    postgres_password: str = getenv("POSTGRES_PASSWORD", "change_me")
    postgres_host: str = getenv("POSTGRES_HOST", "127.0.0.1")
    postgres_port: str = getenv("POSTGRES_PORT", "5432")
    database_url: str | None = getenv("DATABASE_URL")

    secret_key: str = getenv("SECRET_KEY", "change-this-secret-key")
    access_token_expire_minutes: int = int(getenv("ACCESS_TOKEN_EXPIRE_MINUTES", "1440"))

    ai_provider: str = getenv("AI_PROVIDER", "timeweb")
    ai_api_key: str | None = getenv("AI_API_KEY")
    ai_base_url: str | None = getenv("AI_BASE_URL")
    ai_agent_id: str | None = getenv("AI_AGENT_ID")
    ai_model: str = getenv("AI_MODEL", "default")
    ai_request_timeout_seconds: float = float(getenv("AI_REQUEST_TIMEOUT_SECONDS", "30"))

    proverkacheka_api_token: str | None = getenv("PROVERKACHEKA_API_TOKEN")
    proverkacheka_api_url: str = getenv(
        "PROVERKACHEKA_API_URL",
        "https://proverkacheka.com/api/v1/check/get",
    )
    proverkacheka_request_timeout_seconds: float = float(
        getenv("PROVERKACHEKA_REQUEST_TIMEOUT_SECONDS", "20")
    )

    @property
    def sqlalchemy_database_uri(self) -> str:
        if self.database_url:
            return self.database_url
        return (
            "postgresql+asyncpg://"
            f"{self.postgres_user}:{self.postgres_password}"
            f"@{self.postgres_host}:{self.postgres_port}/{self.postgres_db}"
        )


@lru_cache
def get_settings() -> Settings:
    return Settings()
